package com.mc.application.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least 1 digit, 1 lowercase letter, 1 uppercase letter, and 1 special character"
    )
    private String password;

    @NotBlank(message = "Confirm Password can't be blank")
    private String confirmPassword;

    @NotBlank(message = "Full Name can't be blank")
    private String fullName;

    private String inviteToken;

}
