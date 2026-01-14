package com.mc.application.iam.service.impl;

import com.mc.application.iam.dto.request.ChangePasswordRequest;
import com.mc.application.iam.dto.request.UpdateProfileRequest;
import com.mc.application.iam.dto.response.MessageResponse;
import com.mc.application.iam.dto.response.UserResponse;
import com.mc.application.iam.mapper.UserDtoMapper;
import com.mc.application.iam.service.UserAppService;
import com.mc.domain.iam.exception.UserNotFoundException;
import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.UserRepository;
import com.mc.domain.iam.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * User Application Service Implementation - IAM Bounded Context
 */
@Service("iamUserAppService")
@RequiredArgsConstructor
@Transactional
public class UserAppServiceImpl implements UserAppService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;
    @Qualifier("iamUserDomainService")
    private final UserDomainService userDomainService;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile() {
        User user = getCurrentUser();
        return userDtoMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));
        return userDtoMapper.toResponse(user);
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User currentUser = userDomainService.getCurrentUser(getCurrentUserEmail());
        
        User savedUser = userDomainService.updateProfile(
                currentUser,
                request.getFullName(),
                request.getAvatarUrl(),
                request.getAddress(),
                request.getPhone(),
                request.getJobTitle()
        );

        return userDtoMapper.toResponse(savedUser);
    }

    @Override
    public MessageResponse changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        
        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return MessageResponse.error("New password and confirmation do not match");
        }
        
        userDomainService.changePassword(user, user.getPassword(), request.getNewPassword());
        
        return MessageResponse.success("Password changed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String keyword) {
        UUID currentUserId = getCurrentUserId();
        // Note: This would need a search method in the repository
        // For now, return empty list as placeholder
        return List.of();
    }

    private User getCurrentUser() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));
    }

    private UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
