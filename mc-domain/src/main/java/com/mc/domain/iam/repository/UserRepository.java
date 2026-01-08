package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    // Tìm kiếm dựa trên Value Object Email
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);

}
