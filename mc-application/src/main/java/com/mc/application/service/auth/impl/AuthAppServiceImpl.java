package com.mc.application.service.auth.impl;

import com.mc.application.model.auth.LoginRequest;
import com.mc.application.model.auth.RegisterRequest;
import com.mc.application.model.auth.RegisterResponse;
import com.mc.application.service.auth.AuthAppService;
import com.mc.domain.model.entity.*;
import com.mc.domain.repository.TeamRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.repository.UserRoleDomainRepository;
import com.mc.domain.repository.WorkspaceRepository;
import com.mc.domain.service.AuthDomainService;
import com.mc.domain.service.RoleDomainService;
import com.mc.infrastructure.config.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthAppServiceImpl implements AuthAppService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthDomainService authDomainService;
    private final RoleDomainService roleDomainService;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserRoleDomainRepository userRoleDomainRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthAppServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AuthDomainService authDomainService, RoleDomainService roleDomainService, UserRepository userRepository, TeamRepository teamRepository, UserRoleDomainRepository userRoleDomainRepository, WorkspaceRepository workspaceRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authDomainService = authDomainService;
        this.roleDomainService = roleDomainService;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.userRoleDomainRepository = userRoleDomainRepository;
        this.workspaceRepository = workspaceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginRequest loginRequest) {

        // Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);

    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        // 1. Validate input
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Create User Aggregate Root
        User user = User.create(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "ACTIVE"
        );

        // Persist using Repository
        userRepository.save(user);

        // Create default team
        Team team = Team.createDefault(user.getFullName(), user);
        teamRepository.save(team);

        // Assign default role (Domain rule)
        Role defaultRole = roleDomainService.getDefaultRole();
//        user.assignRole(defaultRole, team);

        UserRole userRole = UserRole.of(user, defaultRole, team);
        userRoleDomainRepository.save(userRole);

        Workspace workspace = Workspace.create("Default Workspace", user, team);
        workspaceRepository.save(workspace);

        // Return response
        return new RegisterResponse(
                userRole.getUser().getId(),
                userRole.getTeam().getId(),
                userRole.getRole().getName()
        );
    }

}
