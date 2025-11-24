package com.mc.controller.http.auth;

import com.mc.application.model.auth.*;
import com.mc.application.service.auth.AuthAppService;
import com.mc.domain.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthAppService authAppService;

    public AuthController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Retrieve user by email
        String token = authAppService.login(request);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        RegisterResponse response = authAppService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        authAppService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Forgot password request processed");

    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authAppService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password reset successful");
    }
}
