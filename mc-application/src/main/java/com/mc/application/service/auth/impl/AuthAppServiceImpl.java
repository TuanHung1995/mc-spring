package com.mc.application.service.auth.impl;

import com.mc.application.mapper.UserMapper;
import com.mc.application.model.auth.*;
import com.mc.application.service.auth.AuthAppService;
import com.mc.domain.model.entity.*;
import com.mc.domain.port.MailSender;
import com.mc.domain.repository.*;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.InviteDomainService;
import com.mc.domain.service.RoleDomainService;
import com.mc.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthAppServiceImpl implements AuthAppService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final UserMapper userMapper;

    private final AuthDomainService authDomainService;
    private final RoleDomainService roleDomainService;
    private final InviteDomainService inviteDomainService;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            authDomainService.resetFailedLogin(loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.generateToken(authentication);
            RefreshToken refreshToken = authDomainService.createRefreshToken(loginRequest.getEmail());

            return new JwtAuthResponse(accessToken, refreshToken.getToken(), "Bearer");

        } catch (BadCredentialsException e) {
            authDomainService.handleFailedLogin(loginRequest.getEmail());
            throw e;
        } catch (LockedException e) {
            throw new RuntimeException("Account is locked. Please check your email to unlock.");
        }

    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        User user = authDomainService.register(
                request.getEmail(),
                request.getPassword(),
                request.getConfirmPassword(),
                request.getFullName(),
                request.getInviteToken()
        );

        return userMapper.toRegisterResponse(user);

    }

    @Override
    public void forgotPassword(String email) {
        authDomainService.forgotPassword(email);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmNewPassword) {
        authDomainService.resetPassword(token, newPassword, confirmNewPassword);
    }

    @Override
    public JwtAuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(authDomainService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    // Tạo Access Token mới
                    String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());

                    JwtAuthResponse response = new JwtAuthResponse();
                    response.setAccessToken(token);
                    response.setRefreshToken(request.getRefreshToken()); // Giữ nguyên refresh token cũ (hoặc rotate mới tùy bạn)
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Override
    public void logout(String accessToken) {
        // accessToken thường có prefix "Bearer ", cần cắt bỏ
        String token = accessToken;
        if (accessToken.startsWith("Bearer ")) {
            token = accessToken.substring(7);
        }

        // Lấy email từ token để xóa refresh token tương ứng
        String email = jwtTokenProvider.getUsername(token);

        authDomainService.logout(token, email);
    }

    @Override
    public void unlockAccount(String token) {
        authDomainService.unlockAccount(token);
    }

}
