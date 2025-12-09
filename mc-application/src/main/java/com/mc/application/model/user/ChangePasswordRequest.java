package com.mc.application.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Old Password can't be blank")
    private String oldPassword;

    @NotBlank(message = "New Password can't be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least 1 digit, 1 lowercase letter, 1 uppercase letter, and 1 special character"
    )
    private String newPassword;

    @NotBlank(message = "Confirm New Password can't be blank")
    private String confirmNewPassword;

}
