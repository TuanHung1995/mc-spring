package com.mc.application.iam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Verify Email Request DTO - IAM Bounded Context
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailRequest {

    private String verificationCode;

}
