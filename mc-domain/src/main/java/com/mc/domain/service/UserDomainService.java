package com.mc.domain.service;

import com.mc.domain.model.entity.User;

public interface UserDomainService {

    User updateProfile(Long userId, String fullName, String phone, String address, String jobTitle, String birthday);

    User findUserById(Long userId);

    String changePassword(Long userId, String oldPassword, String newPassword, String confirmNewPassword);
}
