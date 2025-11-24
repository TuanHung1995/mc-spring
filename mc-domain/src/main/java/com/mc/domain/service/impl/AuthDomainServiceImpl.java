package com.mc.domain.service.impl;

import com.mc.domain.model.entity.*;
import com.mc.domain.repository.TeamRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.repository.UserRoleDomainRepository;
import com.mc.domain.repository.WorkspaceRepository;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.RoleDomainService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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
                "ACTIVE"
        );
        userRepository.save(user);

        // Create default team
        Team team = Team.createDefault(user.getFullName(), user);
        teamRepository.save(team);

        // Create Default Workspace
        Workspace workspace = Workspace.create("Default Workspace", user, team);
        workspaceRepository.save(workspace);

        // Assign default role (Domain rule)
        Role defaultRole = roleDomainService.getDefaultRole();
        // user.assignRole(defaultRole, team);

        UserRole userRole = UserRole.of(user, defaultRole, team);
        userRoleDomainRepository.save(userRole);

        return user;
    }

    @Override
    public void forgotPassword(String email) {
        userRepository.forgotPassword(email);
    }
}
