package com.mc.controller.http.auth;

import com.mc.application.model.auth.*;
import com.mc.application.service.auth.AuthAppService;
import com.mc.application.service.invite.InviteAppService;
import com.mc.domain.model.entity.User;
import com.mc.infrastructure.constant.SecurityConstants;
import com.mc.infrastructure.utils.CookieUtils;
import com.nimbusds.openid.connect.sdk.LogoutRequest;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthAppService authAppService;
    private final InviteAppService inviteAppService;
    private final CookieUtils cookieUtils;

    private static final String AUTH_RATE_LIMITER = "authRateLimiter";

    @PostMapping("/login")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(authAppService.login(request));
        // 1. Gọi service login để lấy token như cũ
        JwtAuthResponse tokenData = authAppService.login(request);

        // 2. Tạo Cookies
        ResponseCookie accessCookie = cookieUtils.createAccessTokenCookie(tokenData.getAccessToken());
        ResponseCookie refreshCookie = cookieUtils.createRefreshTokenCookie(tokenData.getRefreshToken());

        // 3. Trả về Response kèm Cookies
        // Body có thể trả về thông tin User cơ bản để Frontend hiển thị ngay
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("Login successful");
    }

    @PostMapping("/register")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authAppService.register(request);

        if (request.getInviteToken() != null && !request.getInviteToken().isEmpty()) {
            inviteAppService.acceptInvitation(request.getInviteToken());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        authAppService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Forgot password request processed");

    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authAppService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        return ResponseEntity.ok(authAppService.refreshToken(request));
        // 1. Lấy refresh token từ Cookie
        String refreshToken = cookieUtils.getCookieValue(request, CookieUtils.REFRESH_TOKEN_COOKIE_NAME);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh Token is missing");
        }

        // 2. Gọi Service xử lý (Cần sửa Service để nhận String thay vì Object nếu cần)
        RefreshTokenRequest tokenRequest = new RefreshTokenRequest();
        tokenRequest.setRefreshToken(refreshToken);
        JwtAuthResponse newTokenData = authAppService.refreshToken(tokenRequest);

        // 3. Set lại Cookie Access Token mới
        ResponseCookie accessCookie = cookieUtils.createAccessTokenCookie(newTokenData.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body("Token refreshed");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        // Delete token in DB
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token == null) {
            token = cookieUtils.getCookieValue(request, CookieUtils.ACCESS_TOKEN_COOKIE_NAME);
        }

        if (token != null) {
            authAppService.logout(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
        }

        // Delete Cookies for Clients
        ResponseCookie deleteAccess = cookieUtils.deleteCookie(CookieUtils.ACCESS_TOKEN_COOKIE_NAME);
        ResponseCookie deleteRefresh = cookieUtils.deleteCookie(CookieUtils.REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccess.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefresh.toString())
                .body("Logged out successfully");

    }

    @PostMapping("/unlock-account")
    @RateLimiter(name = AUTH_RATE_LIMITER)
    public ResponseEntity<?> unlockAccount(@RequestParam String token) {
        authAppService.unlockAccount(token);
        return ResponseEntity.ok("Account unlocked successfully. You can now login.");
    }

}
