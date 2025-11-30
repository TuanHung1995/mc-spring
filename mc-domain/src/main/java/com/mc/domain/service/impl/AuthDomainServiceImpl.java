package com.mc.domain.service.impl;

import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import com.mc.domain.repository.*;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.RoleDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthDomainServiceImpl implements AuthDomainService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleDomainService roleDomainService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private final Long refreshTokenDurationMs;

    @Override
    public User register(String email, String password, String confirmPassword, String fullName) {

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Validate input
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Create User Aggregate Root
        User user = User.create(
                fullName,
                email,
                passwordEncoder.encode(password),
                AccountStatus.ACTIVE,
                AuthProvider.LOCAL
        );
        userRepository.save(user);

        // Initialize User Data
        initUserData(user);

        return user;
    }

    @Override
    public User processOAuthPostLogin(String email, String name, String imageUrl) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Case 1: Register new user from Google
            user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setProvider(AuthProvider.GOOGLE);
            user.setStatus(AccountStatus.ACTIVE);
            user.setAvatarUrl(imageUrl);
            userRepository.save(user);

            // Tái sử dụng logic tạo data mặc định
            initUserData(user);

            log.info("Registered new user via Google OAuth: {}", email);
        } else {
            // Case 2: Update existing user (optional)
            user.setFullName(name);
            user.setAvatarUrl(imageUrl);
            // Nếu user cũ là LOCAL, có thể update thành GOOGLE hoặc giữ nguyên tùy nghiệp vụ
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public void forgotPassword(String email) {
        userRepository.forgotPassword(email);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmNewPassword) {
        userRepository.resetPassword(token, newPassword, confirmNewPassword);
    }

    private void initUserData(User user) {
        // Create default team
        Team team = Team.createDefault(user.getFullName(), user);
        teamRepository.save(team);

        // Assign Role to User in Team
        Role role = roleDomainService.getDefaultRole();
        TeamMember teamMember = TeamMember.createDefault(user, team, role);
        teamMemberRepository.save(teamMember);

        // Create Default Workspace
        Workspace workspace = Workspace.create("DEFAULT_WORKSPACE", user, team);
        workspaceRepository.save(workspace);

    }

    @Override
    public RefreshToken createRefreshToken(String email) {
        /* Delete existing refresh tokens for the user */
        refreshTokenRepository.deleteByUserEmail(email);

        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteByUserEmail(token.getUser().getEmail());
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
