package com.mc.domain.service;

import com.mc.domain.model.entity.User;

public interface AuthDomainService {

    User register(String email, String password, String confirmPassword, String fullName);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmNewPassword);

}
