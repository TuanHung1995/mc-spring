package com.mc.application.service.auth;

import com.mc.application.model.auth.LoginRequest;

public interface AuthAppService {

    String login(LoginRequest loginRequest);

}
