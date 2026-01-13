package com.mc.application.iam.service;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;

/**
 * Auth Application Service Interface - IAM Bounded Context
 * Orchestrates authentication use cases.
 */
public interface AuthAppService {

    /**
     * Authenticate user and return JWT tokens.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Register a new user account.
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * Refresh access token using refresh token.
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Logout user and invalidate tokens.
     */
    MessageResponse logout(String accessToken);

    /**
     * Initiate password reset flow.
     */
    MessageResponse forgotPassword(String email);

    /**
     * Reset password using reset token.
     */
    MessageResponse resetPassword(ResetPasswordRequest request);

    /**
     * Unlock a locked user account.
     */
    MessageResponse unlockAccount(String token);
}
