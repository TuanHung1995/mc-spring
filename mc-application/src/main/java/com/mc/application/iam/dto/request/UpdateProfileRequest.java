package com.mc.application.iam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Profile Request DTO - IAM Bounded Context
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    private String fullName;
    private String avatarUrl;
    private String bio;
    private String phone;
    private String address;
    private String jobTitle;
}
