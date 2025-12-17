package com.mc.domain.service.impl;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import com.mc.domain.port.MailSender;
import com.mc.domain.port.TokenHelperPort;
import com.mc.domain.repository.*;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.RoleDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
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
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenHelperPort tokenHelperPort;
    private final MailSender mailSender;

    @Value("${constants.max-failed-login-attempts}")
    private int MAX_FAILED_ATTEMPTS;

    @Value("${constants.frontend}")
    private String appUrl;

    @Value("${jwt.expiration}")
    private long refreshTokenDurationMs;

    @Override
    public User register(String email, String password, String confirmPassword, String fullName, String inviteToken) {

        if (!password.equals(confirmPassword)) {
            throw new BusinessLogicException("Passwords do not match");
        }

        // Validate input
        if (userRepository.existsByEmail(email)) {
            throw new BusinessLogicException("Email already in use");
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
        if (inviteToken == null && inviteToken.isEmpty()) {
            initUserData(user);
        }

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

        WorkspaceMember workspaceMember = new WorkspaceMember();
        workspaceMember.setUser(user);
        workspaceMember.setWorkspace(workspace);
        workspaceMember.setRole(role);
        workspaceMember.setJoinedAt(new Date());

        workspaceMemberRepository.save(workspaceMember);

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

    @Override
    @Transactional
    public void logout(String accessToken, String email) {
        // 1. Xóa Refresh Token
        refreshTokenRepository.deleteByUserEmail(email);

        // 2. Lấy thời gian hết hạn thông qua Port
        Date expiration = tokenHelperPort.getExpirationDateFromToken(accessToken);

        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.setToken(accessToken);
        blacklist.setExpiryDate(expiration.toInstant());

        tokenBlacklistRepository.save(blacklist);
    }

    @Override
    public void handleFailedLogin(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return;

        // Nếu đã khóa rồi thì không làm gì thêm (hoặc có thể gửi lại mail)
        if (AccountStatus.LOCKED.equals(user.getStatus())) {
            return;
        }

        int attempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        user.setFailedLoginAttempts(attempts + 1);

        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setStatus(AccountStatus.LOCKED);
            String token = UUID.randomUUID().toString();
            user.setUnlockToken(token);

            // Gửi email
            String unlockLink = appUrl + "/unlock-account?token=" + token; // Link trỏ về Frontend
            String subject = "Account Locked - Action Required";
            String body = "Your account has been locked due to multiple failed login attempts.\n"
                    + "Please click the link below to unlock your account:\n" + unlockLink;

            mailSender.send(email, subject, body);
        }

        userRepository.save(user);
    }

    @Override
    public void resetFailedLogin(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }
    }

    @Override
    public void unlockAccount(String token) {
        User user = userRepository.findByUnlockToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("User", "unlockToken", token));

        user.setStatus(AccountStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setUnlockToken(null);
        userRepository.save(user);
    }
}
