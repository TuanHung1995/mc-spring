package com.mc.application.service.user.impl;

import com.mc.application.model.user.UpdateProfileRequest;
import com.mc.application.service.user.UserAppService;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.User;
import com.mc.domain.service.UserDomainService;
import com.mc.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAppServiceImpl implements UserAppService {

    private final UserDomainService userDomainService;

    @Override
    public User updateProfile(UpdateProfileRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId;

        if (principal instanceof CustomUserDetails) {
            currentUserId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new ResourceNotFoundException("User", "id", principal);
        }

        return userDomainService.updateProfile(
                currentUserId,
                request.getFullName(),
                request.getPhone(),
                request.getAddress(),
                request.getJobTitle(),
                request.getBirthday()
        );

    }

}
