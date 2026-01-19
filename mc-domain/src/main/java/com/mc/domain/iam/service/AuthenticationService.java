package com.mc.domain.iam.service;

import com.mc.domain.iam.model.RefreshToken;
import com.mc.domain.iam.model.User;

import java.util.UUID;

/**
 * Authentication Domain Service Interface - IAM Bounded Context
 * Contains domain logic for authentication workflows.
 */
public interface AuthenticationService {

    /**
     * Register a new user with email and password.
     */
    User registerWithEmail(String email, String rawPassword, String fullName);

    /**
     * Validate user credentials and return the user if valid.
     */
    User authenticate(String email, String rawPassword);

    /**
     * Create a new refresh token for the user.
     */
    RefreshToken createRefreshToken(UUID userId);

    /**
     * Verify and return a refresh token if valid.
     */
    RefreshToken verifyRefreshToken(String token);

    /**
     * Invalidate a refresh token (logout).
     */
    void invalidateRefreshToken(String token);

    /**
     * Blacklist an access token.
     */
    void blacklistAccessToken(String accessToken);

    /**
     * Check if an access token is blacklisted.
     */
    boolean isTokenBlacklisted(String accessToken);

    /**
     * Handle failed login attempt.
     */
    void handleFailedLogin(String email, String token);

    /**
     * Reset failed login counter.
     */
    void resetFailedLogin(String email);

    /**
     * Initiate password reset flow.
     */
    void initiatePasswordReset(String email);

    /**
     * Reset password with token.
     */
    void resetPassword(String resetToken, String newPassword);

    /**
     * Unlock a locked account.
     */
    void unlockAccount(String email, String unlockToken);

    /**
     * Send verification email code to user.
     */
    void sendEmailVerificationCode(String email);

    /**
     * Verify user's email with code.
     */
    void verifyEmail(String email, String verificationCode);
}
