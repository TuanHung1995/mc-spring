package com.mc.controller.http.iam;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;
import com.mc.application.iam.service.AuthAppService;
import com.mc.infrastructure.utils.CookieUtils;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - IAM Bounded Context
 * Handles authentication endpoints using Clean Architecture.
 */
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@Slf4j
public class IamAuthController {

    private final AuthAppService authAppService;
    private final CookieUtils cookieUtils;

    private static final String AUTH_RATE_LIMITER = "authRateLimiter";

    @PostMapping("/login")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authAppService.login(request);

        ResponseCookie accessCookie = cookieUtils.createAccessTokenCookie(response.getAccessToken());
        ResponseCookie refreshCookie = cookieUtils.createRefreshTokenCookie(response.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response);
    }

    @PostMapping("/register")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authAppService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        MessageResponse response = authAppService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        MessageResponse response = authAppService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = cookieUtils.getCookieValue(request, CookieUtils.REFRESH_TOKEN_COOKIE_NAME);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        RefreshTokenRequest tokenRequest = new RefreshTokenRequest(refreshToken);
        AuthResponse response = authAppService.refreshToken(tokenRequest);

        ResponseCookie accessCookie = cookieUtils.createAccessTokenCookie(response.getAccessToken());
        ResponseCookie newRefreshCookie = cookieUtils.createRefreshTokenCookie(response.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        String token = cookieUtils.getCookieValue(request, CookieUtils.ACCESS_TOKEN_COOKIE_NAME);

        MessageResponse message = null;
        
        if (token != null) {
            message = authAppService.logout(token);
        }

        ResponseCookie deleteAccess = cookieUtils.deleteCookie(CookieUtils.ACCESS_TOKEN_COOKIE_NAME);
        ResponseCookie deleteRefresh = cookieUtils.deleteCookie(CookieUtils.REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccess.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefresh.toString())
                .body(message);
    }

    @PostMapping("/unlock-account")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<MessageResponse> unlockAccount(@RequestParam String token) {
        MessageResponse response = authAppService.unlockAccount(token);
        return ResponseEntity.ok(response);
    }
}
