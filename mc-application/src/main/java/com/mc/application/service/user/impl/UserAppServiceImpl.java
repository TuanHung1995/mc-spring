package com.mc.application.service.user.impl;

import com.mc.application.mapper.UserMapper;
import com.mc.application.model.user.*;
import com.mc.application.service.user.UserAppService;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.User;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.UserDomainService;
import com.mc.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAppServiceImpl implements UserAppService {

    private final UserDomainService userDomainService;
    private final UserMapper userMapper;
    private final UserContextPort userContextPort;

    @Override
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {

        User user = userDomainService.updateProfile(
                getCurrentUserId(),
                request.getFullName(),
                request.getPhone(),
                request.getAddress(),
                request.getJobTitle(),
                request.getBirthday()
        );

        return userMapper.toUpdateProfileResponse(user);

    }

    @Override
    public UserProfileResponse getMyProfile() {

        User user = userDomainService.findUserById(getCurrentUserId());

        return userMapper.toUserProfileResponse(user);

    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {

        return new ChangePasswordResponse(
                userDomainService.changePassword(
                        getCurrentUserId(),
                        request.getOldPassword(),
                        request.getNewPassword(),
                        request.getConfirmNewPassword()
                )
        );

    }

    @Override
    public List<UserProfileResponse> searchUsers(String keyword) {

        List<User> users = userDomainService.search(keyword, getCurrentUserId());

        return users.stream()
                .map(userMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

    private Long getCurrentUserId() {
        return userContextPort.getCurrentUserId();
    }

}
