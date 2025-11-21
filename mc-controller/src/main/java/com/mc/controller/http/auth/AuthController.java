package com.mc.controller.http.auth;

import com.mc.application.model.auth.JwtAuthResponse;
import com.mc.application.model.auth.LoginRequest;
import com.mc.application.service.auth.AuthAppService;
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
}
