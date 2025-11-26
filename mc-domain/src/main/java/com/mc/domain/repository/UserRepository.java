package com.mc.domain.repository;

import com.mc.domain.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository {

    // Define methods for user repository, e.g.:
    Optional<User> findByEmail(String email);
//    User findByEmail(String email, boolean throwIfNotFound);
    User save(String email, String password, String fullName);
    void save(User user);
    boolean existsByEmail(String email);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword, String confirmNewPassword);
    // List<User> findAll();
    // void deleteById(Long id);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

}
