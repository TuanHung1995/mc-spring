package com.mc.controller.http.auth;

import com.mc.application.model.auth.JwtAuthResponse;
import com.mc.application.model.auth.LoginRequest;
import com.mc.application.model.auth.RegisterRequest;
import com.mc.application.model.auth.RegisterResponse;
import com.mc.application.service.auth.AuthAppService;
import com.mc.domain.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
