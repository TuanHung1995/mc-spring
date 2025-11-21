package com.mc.domain.repository;

import com.mc.domain.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {

    // Define methods for user repository, e.g.:
    Optional<User> findByEmail(String email);
//    User save(String email, String password, String address, String phone);
//    void forgotPassword(String email);
//    void resetPassword(String token, String newPassword, String confirmNewPassword);
    // List<User> findAll();
    // void deleteById(Long id);

    // Example method signature
//    String getUserRole(String email);  // Example method to get user role by email

}
