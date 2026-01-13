package com.mc.application.iam.mapper;

import com.mc.application.iam.dto.response.UserResponse;
import com.mc.domain.iam.model.User;
import org.springframework.stereotype.Component;

/**
 * User DTO Mapper - Application Layer
 * Converts between domain User model and DTOs.
 */
@Component
public class UserDtoMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmailValue())
                .fullName(user.getProfile().getFullName())
                .avatarUrl(user.getProfile().getAvatarUrl())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
