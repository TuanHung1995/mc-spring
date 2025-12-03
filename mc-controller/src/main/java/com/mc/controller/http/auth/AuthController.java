package com.mc.controller.http.auth;

import com.mc.application.model.auth.*;
import com.mc.application.service.auth.AuthAppService;
import com.mc.application.service.invite.InviteAppService;
import com.mc.domain.model.entity.User;
import com.nimbusds.openid.connect.sdk.LogoutRequest;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthAppService authAppService;
    private final InviteAppService inviteAppService;

    private static final String AUTH_RATE_LIMITER = "authRateLimiter";

    public AuthController(AuthAppService authAppService, InviteAppService inviteAppService) {
        this.authAppService = authAppService;
        this.inviteAppService = inviteAppService;
    }

    @PostMapping("/login")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authAppService.login(request));
    }

    @PostMapping("/register")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        RegisterResponse response = authAppService.register(request);

        if (request.getInviteToken() != null && !request.getInviteToken().isEmpty()) {
            inviteAppService.acceptInvitation(request.getInviteToken());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        authAppService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Forgot password request processed");

    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authAppService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authAppService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isEmpty()) {
            authAppService.logout(authHeader);
            return ResponseEntity.ok("Logged out successfully");
        }

        return ResponseEntity.badRequest().body("No Authorization header found");

    }

    @PostMapping("/unlock-account")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> unlockAccount(@RequestParam String token) {
        authAppService.unlockAccount(token);
        return ResponseEntity.ok("Account unlocked successfully. You can now login.");
    }

    // return default login page
    @GetMapping("/login")
    public String getLoginPage() {
        return "Please use POST /api/v1/auth/login to login.";
    }
}
