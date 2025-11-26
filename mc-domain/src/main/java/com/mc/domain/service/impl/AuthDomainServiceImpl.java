package com.mc.domain.service.impl;

import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import com.mc.domain.repository.TeamRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.repository.UserRoleDomainRepository;
import com.mc.domain.repository.WorkspaceRepository;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.RoleDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthDomainServiceImpl implements AuthDomainService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleDomainService roleDomainService;
    private final UserRoleDomainRepository userRoleDomainRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDomainServiceImpl(UserRepository userRepository, TeamRepository teamRepository, WorkspaceRepository workspaceRepository, RoleDomainService roleDomainService, UserRoleDomainRepository userRoleDomainRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.workspaceRepository = workspaceRepository;
        this.roleDomainService = roleDomainService;
        this.userRoleDomainRepository = userRoleDomainRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
                AccountStatus.ACTIVE.name(),
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
            user.setStatus(AccountStatus.ACTIVE.name());
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

        // Create Default Workspace
        Workspace workspace = Workspace.create("DEFAULT_WORKSPACE", user, team);
        workspaceRepository.save(workspace);

        // Assign default role
        Role defaultRole = roleDomainService.getDefaultRole();
        UserRole userRole = UserRole.of(user, defaultRole, team);
        userRoleDomainRepository.save(userRole);
    }
}
