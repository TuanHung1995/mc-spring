package com.mc.domain.service;

import com.mc.domain.model.entity.User;

import java.util.List;

public interface UserDomainService {

    User updateProfile(Long userId, String fullName, String phone, String address, String jobTitle, String birthday);

    User findUserById(Long userId);

    User findUserByEmail(String email);

    String changePassword(Long userId, String oldPassword, String newPassword, String confirmNewPassword);

    List<User> search(String keyword, Long currentUserId);

}
