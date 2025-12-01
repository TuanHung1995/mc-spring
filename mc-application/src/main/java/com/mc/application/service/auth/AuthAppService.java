package com.mc.application.service.auth;

import com.mc.application.model.auth.*;
import com.mc.domain.model.entity.User;

public interface AuthAppService {

    JwtAuthResponse login(LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest registerRequest);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmNewPassword);

    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(String accessToken);
}
