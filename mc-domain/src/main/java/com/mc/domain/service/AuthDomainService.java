package com.mc.domain.service;

import com.mc.domain.model.entity.RefreshToken;
import com.mc.domain.model.entity.User;

public interface AuthDomainService {

    User register(String email, String password, String confirmPassword, String fullName, String inviteToken);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmNewPassword);

    User processOAuthPostLogin(String email, String name, String imageUrl);

    RefreshToken createRefreshToken(String email);

    RefreshToken verifyExpiration(RefreshToken token);

    void logout(String accessToken, String email);

    void handleFailedLogin(String email);

    void resetFailedLogin(String email);

    void unlockAccount(String token);

}
