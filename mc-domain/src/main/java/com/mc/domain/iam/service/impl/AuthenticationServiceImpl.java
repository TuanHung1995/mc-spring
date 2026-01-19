package com.mc.domain.iam.service.impl;

import com.mc.domain.core.port.out.MailSender;
import com.mc.domain.iam.exception.*;
import com.mc.domain.iam.model.*;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.*;
import com.mc.domain.iam.service.AuthenticationService;
import com.mc.domain.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * Authentication Domain Service Implementation - IAM Bounded Context
 * Contains business logic for authentication workflows.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${constants.max-failed-login-attempts}")
    private int MAX_FAILED_ATTEMPTS;

    @Qualifier("iamMailSender")
    private final MailSender mailSender;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshTokenExpirationMs;

    @Value("${jwt.expiration:3600000}")
    private long accessTokenExpirationMs;

    @Value("${jwt.password-reset-expiration:3600000}")
    private long passwordResetExpirationMs;

    @Value("${server.port}")
    private String serverPort;

    @Value("${constants.dev-url}")
    private String devUrl;

    @Value("${constants.auth-api-url-v2}")
    private String authApiUrl;

    @Value("${constants.development}")
    private boolean isDevelopment;

    @Value("${constants.frontend}")
    private String appUrl;

    @Override
    public User registerWithEmail(String email, String rawPassword, String fullName) {
        Email emailVO = new Email(email);

        // Check if user already exists
        if (userRepository.existsByEmail(emailVO)) {
            throw UserAlreadyExistsException.withEmail(email);
        }

        // Create and save user
        String passwordHash = passwordEncoder.encode(rawPassword);
        User user = User.registerWithEmail(emailVO, passwordHash, fullName);

        return userRepository.save(user);
    }

    @Override
    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new AccountLockedException("Account is not active");
        }

        return user;
    }

    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        // Delete existing tokens for this user
        refreshTokenRepository.deleteByUserId(userId);

        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.create(userId, refreshTokenExpirationMs);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidTokenException::new);

        refreshToken.validate();

        return refreshToken;
    }

    @Override
    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        log.info("Refresh token invalidated");
    }

    @Override
    public void blacklistAccessToken(String accessToken) {
        TokenBlacklist blacklist = TokenBlacklist.create(
                accessToken,
                Instant.now().plusMillis(accessTokenExpirationMs)
        );
        tokenBlacklistRepository.save(blacklist);
        log.info("Access token blacklisted");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTokenBlacklisted(String accessToken) {
        return tokenBlacklistRepository.existsByToken(accessToken);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFailedLogin(String email, String token) {

        log.warn("Failed login attempt for email: {}", email);
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        log.info("Attempt for user: {}", user.getFailedLoginAttempts());
        if (AccountStatus.LOCKED.equals(user.getStatus())) {
            return;
        }

        user.incrementFailedLoginAttempts();

        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.deactivate();
            user.generateUnlockToken(token);

            sendMail(email, "Account Locked - Action Required", isDevelopment,
                    authApiUrl, "/unlock-account?token=", serverPort,
                    devUrl, appUrl + "/",
                    user.getUnlockToken(),
                    """
                            Your account has been locked due to multiple failed login attempts.
                            Please click the link below to unlock your account:
                            """, true);

            log.info("Account locked due to too many failed attempts for email: {}", email);
            log.debug("Unlock token (dev): {}", user.getUnlockToken());
        }

        log.info("Check failed attempt: {}", user.getFailedLoginAttempts());
        User updatedUser = userRepository.save(user);

        log.info("Check hibernate: {}", updatedUser.getFailedLoginAttempts());
    }

    @Override
    public void resetFailedLogin(String email) {

        log.info("Failed login counter reset for email: {}", email);
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow();

        user.resetFailedLoginAttempts();
        userRepository.save(user);

    }

    @Override
    public void initiatePasswordReset(String email) {
        userRepository.findByEmail(new Email(email)).ifPresent(user -> {
            // Delete any existing tokens
            passwordResetTokenRepository.deleteByUserId(user.getId());

            // Create new reset token
            PasswordResetToken resetToken = PasswordResetToken.create(
                    user.getId(),
                    passwordResetExpirationMs
            );
            passwordResetTokenRepository.save(resetToken);

            sendMail(email, "Password Reset Request", isDevelopment,
                    authApiUrl, "/reset-password?token=", serverPort,
                    devUrl, appUrl + "/",
                    resetToken.getToken(),
                    "Click the link below to reset your password:\n", true);

            log.info("Password reset initiated for user: {}", email);
            log.debug("Reset token (dev): {}", resetToken.getToken());
        });
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        token.validate();

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> UserNotFoundException.withId(token.getUserId()));

        String newPasswordHash = passwordEncoder.encode(newPassword);
        user.changePassword(newPasswordHash);
        userRepository.save(user);

        token.markAsUsed();
        passwordResetTokenRepository.save(token);

        log.info("Password reset completed for user ID: {}", user.getId());
    }

    @Override
    public void unlockAccount(String email, String unlockToken) {
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        user.activate();
        user.generateUnlockToken(null);
        user.resetFailedLoginAttempts();
        userRepository.save(user);

        log.info("Account unlock requested with token");
    }

    @Override
    public void sendEmailVerificationCode(String email) {

        String verificationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();

        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        verificationCodeRepository.save(
                VerificationCode.create(
                        user.getId(),
                        verificationCode,
                        Instant.now().plusSeconds(5 * 60).atZone(ZoneId.systemDefault()).toLocalDateTime()
                )
        );

        sendMail(email, "Email Verification Code", isDevelopment,
                null, null, null,
                null, null,
                null,
                "Use the following code to verify your email address: " + verificationCode, false);

        log.info("Verification code sent to email: {}", email);
        log.debug("Verification code (dev): {}", verificationCode);

    }

    @Override
    public void verifyEmail(String email, String verificationCode) {
        User cuurentUser = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));

        List<VerificationCode> listCode = verificationCodeRepository.findAllByUserId(cuurentUser.getId())
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();

        VerificationCode code = getVerificationCode(verificationCode, listCode);
        verificationCodeRepository.save(code);

        cuurentUser.verifyEmail();

        userRepository.save(cuurentUser);

        log.info("Email verified for: {}", email);
    }

    private static VerificationCode getVerificationCode(String verificationCode, List<VerificationCode> listCode) {
        VerificationCode code = listCode.isEmpty() ? null : listCode.get(0);

        if (code == null) {
            throw new InvalidTokenException("No verification code found");
        }

        if (!code.getCode().equals(verificationCode)) {
            throw new InvalidTokenException("Invalid verification code");
        }

        if (code.isExpired()) {
            throw new TokenExpiredException("Verification code");
        }

        if (code.isUsed()) {
            throw new InvalidTokenException("Verification code has already been used");
        }

        code.markAsUsed();
        return code;
    }

    private void sendMail(
            String email, String subject, boolean isDev,
            String rootApiUrl, String apiUrl, String port,
            String devUrl, String appUrl, String token, String content, boolean useLink
    ) {

        String rootUrl;

        if (isDev) {
            rootUrl = devUrl + port;
        } else {
            rootUrl = appUrl;
        }

        String link = rootUrl + rootApiUrl + apiUrl + token;
        String body = content + link;

        if (!useLink) {
            body = content;
        }

        mailSender.send(email, subject, body);

    }
}
