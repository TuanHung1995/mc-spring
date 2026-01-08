package com.mc.application.iam.service.impl;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;
import com.mc.application.iam.mapper.UserDtoMapper;
import com.mc.application.iam.service.AuthAppService;
import com.mc.application.model.auth.JwtAuthResponse;
import com.mc.domain.iam.exception.*;
import com.mc.domain.iam.model.PasswordResetToken;
import com.mc.domain.iam.model.RefreshToken;
import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.PasswordResetTokenRepository;
import com.mc.domain.iam.repository.RefreshTokenRepository;
import com.mc.domain.iam.repository.TokenBlacklistRepository;
import com.mc.domain.iam.repository.UserRepository;
import com.mc.domain.iam.model.TokenBlacklist;
import com.mc.domain.iam.service.AuthenticationService;
import com.mc.infrastructure.iam.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Auth Application Service Implementation - IAM Bounded Context
 * Orchestrates authentication use cases using domain models and infrastructure services.
 */
@Slf4j
@Service("iamAuthAppService")
@RequiredArgsConstructor
@Transactional
public class AuthAppServiceImpl implements AuthAppService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    @Qualifier("iamJwtTokenProvider")
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs;

    @Value("${jwt.password-reset-expiration:3600000}")
    private long passwordResetExpirationMs;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = authenticationService.authenticate(request.getEmail(), request.getPassword());

            authenticationService.resetFailedLogin(request.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.generateToken(authentication);
            RefreshToken refreshToken = authenticationService.createRefreshToken(user.getId());

            log.info("User logged in successfully: {}", request.getEmail());

            return AuthResponse.of(
                    accessToken,
                    refreshToken.getToken(),
                    jwtExpirationMs / 1000,
                    userDtoMapper.toResponse(user)
            );

        } catch (BadCredentialsException e) {
            authenticationService.handleFailedLogin(request.getEmail());
            throw new InvalidCredentialsException();
        } catch (LockedException e) {
            throw new AccountLockedException("Account is not active");
        }
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        Email email = new Email(request.getEmail());

        // Check if user exists
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.withEmail(request.getEmail());
        }

        // Create user using domain factory method
        String passwordHash = passwordEncoder.encode(request.getPassword());
        User user = User.registerWithEmail(email, passwordHash, request.getFullName());

        User savedUser = userRepository.save(user);

        log.info("New user registered: {}", email.getValue());

        return RegisterResponse.success(
                savedUser.getId(),
                savedUser.getEmailValue(),
                savedUser.getProfile().getFullName()
        );
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(InvalidTokenException::new);

        // Validate token
        refreshToken.validate();

        // Get user
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> UserNotFoundException.withId(refreshToken.getUserId()));

        // Delete old token and create new one
        refreshTokenRepository.deleteByToken(request.getRefreshToken());
        RefreshToken newRefreshToken = RefreshToken.create(user.getId(), refreshExpirationMs);
        refreshTokenRepository.save(newRefreshToken);

        String accessToken = generateAccessToken(user);

        log.info("Token refreshed for user: {}", user.getEmailValue());

        return AuthResponse.of(
                accessToken,
                newRefreshToken.getToken(),
                jwtExpirationMs / 1000,
                userDtoMapper.toResponse(user)
        );
    }

    @Override
    public MessageResponse logout(String accessToken) {
        // Blacklist the access token
        authenticationService.blacklistAccessToken(accessToken);

        // Delete refresh token for current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("User logged out: {}", email);

        userRepository.findByEmail(new Email(email))
                .ifPresent(user -> {
                    refreshTokenRepository.deleteByUserId(user.getId());
                    log.info("User logged out: {}", email);
                });

        return MessageResponse.success("Logged out successfully");
    }

    @Override
    public MessageResponse forgotPassword(String emailStr) {

        authenticationService.initiatePasswordReset(emailStr);

        // Always return success message to avoid email enumeration
        return MessageResponse.success(
                "If the email exists in our system, a password reset link has been sent"
        );
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return MessageResponse.error("Passwords do not match");
        }

        // Find and validate reset token
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        resetToken.validate();

        // Get user and change password
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> UserNotFoundException.withId(resetToken.getUserId()));

        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        user.changePassword(newPasswordHash);
        userRepository.save(user);

        // Mark token as used
        resetToken.markAsUsed();
        passwordResetTokenRepository.save(resetToken);

        log.info("Password reset successfully for user: {}", user.getEmailValue());

        return MessageResponse.success("Password has been reset successfully. You can now login with your new password.");
    }

    @Override
    public MessageResponse unlockAccount(String token) {
        // Validate JWT token for account unlock
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("Invalid unlock token");
        }

        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        // Activate account
        user.activate();
        userRepository.save(user);

        log.info("Account unlocked for user: {}", email);

        return MessageResponse.success("Account has been unlocked successfully. You can now login.");
    }

    // =================================================================
    // PRIVATE HELPERS
    // =================================================================

    /**
     * Generate JWT access token for authenticated user.
     * Delegates to JwtTokenProvider in infrastructure layer.
     */
    private String generateAccessToken(User user) {
        return jwtTokenProvider.generateTokenWithUserId(
                user.getEmailValue(),
                user.getId()
        );
    }
}
