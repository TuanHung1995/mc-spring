package com.mc.application.iam.service;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;

import java.util.List;
import java.util.UUID;

/**
 * User Application Service Interface - IAM Bounded Context
 * Orchestrates user profile management use cases.
 */
public interface UserAppService {

    /**
     * Get current user's profile.
     */
    UserResponse getMyProfile();

    /**
     * Get user profile by ID.
     */
    UserResponse getUserById(UUID userId);

    /**
     * Update current user's profile.
     */
    UserResponse updateProfile(UpdateProfileRequest request);

    /**
     * Change current user's password.
     */
    MessageResponse changePassword(ChangePasswordRequest request);

    /**
     * Search users by keyword.
     */
    List<UserResponse> searchUsers(String keyword);
}
