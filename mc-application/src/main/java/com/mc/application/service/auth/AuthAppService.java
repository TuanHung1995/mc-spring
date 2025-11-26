package com.mc.application.service.auth;

import com.mc.application.model.auth.LoginRequest;
import com.mc.application.model.auth.RegisterRequest;
import com.mc.application.model.auth.RegisterResponse;
import com.mc.domain.model.entity.User;

public interface AuthAppService {

    String login(LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest registerRequest);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmNewPassword);

}
