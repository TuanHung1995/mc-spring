package com.mc.domain.iam.service;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Domain Service Interface - IAM Bounded Context
 * Contains domain logic that doesn't naturally fit within the User aggregate.
 */
public interface UserDomainService {

    /**
     * Find a user by their ID.
     */
    Optional<User> findById(UUID userId);

    /**
     * Find a user by their email.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Update user profile.
     */
    User updateProfile(User currentUser, String fullName, String avatarUrl, String address, String phone, String jobTitle);

    /**
     * Change user password.
     */
    void changePassword(User user, String oldPassword, String newPassword);

    /**
     * Search users by keyword.
     */
    List<User> searchUsers(String keyword, UUID excludeUserId);

    /**
     * Get the currently authenticated user.
     */
    User getCurrentUser(String email);
}
