package com.mc.application.iam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Register Response DTO - IAM Bounded Context
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private UUID userId;
    private String email;
    private String fullName;
    private String message;

    public static RegisterResponse success(UUID userId, String email, String fullName) {
        return RegisterResponse.builder()
                .userId(userId)
                .email(email)
                .fullName(fullName)
                .message("Registration successful. Please check your email to verify your account.")
                .build();
    }
}
