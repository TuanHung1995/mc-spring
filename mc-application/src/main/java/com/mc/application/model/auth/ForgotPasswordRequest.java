package com.mc.application.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email is not valid")
    private String email;

}
