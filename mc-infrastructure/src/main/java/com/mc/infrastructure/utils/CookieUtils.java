package com.mc.infrastructure.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtils {

    @Value("${jwt.expiration}")
    private long accessTokenDurationMs;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationMs;

    @Value("${app.cookie.secure}")
    private boolean isSecured;

    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    // Tạo HttpOnly Cookie cho Access Token
    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(isSecured)
                /* or "Strict", "None" */
                .sameSite("Lax")
                .path("/")
                .maxAge(accessTokenDurationMs / 1000) // Đổi sang giây
                .build();
    }

    // Tạo HttpOnly Cookie cho Refresh Token
    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(isSecured)
                .sameSite("Lax")
                .path("/")
                .maxAge(refreshTokenDurationMs / 1000) // Ví dụ: 30 ngày
                .build();
    }

    // Xóa Cookie (dùng cho Logout)
    public ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }

    // Lấy giá trị Cookie từ Request
    public String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }
}