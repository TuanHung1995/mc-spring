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
     * Register a new user with email and password.
     */
    User registerWithEmail(String email, String rawPassword, String fullName);

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
    User updateProfile(UUID userId, String fullName, String avatarUrl, String bio);

    /**
     * Change user password.
     */
    void changePassword(UUID userId, String oldPassword, String newPassword);

    /**
     * Search users by keyword.
     */
    List<User> searchUsers(String keyword, UUID excludeUserId);
}
