package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJPAMapper extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

//    User findByEmail(String email, boolean ignoreCase);

    User findByResetToken(String token);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findByUnlockToken(String token);

    @Query("SELECT u FROM User u WHERE u.id != :currentUserId AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchUsers(@Param("keyword") String keyword, @Param("currentUserId") Long currentUserId, Pageable pageable);

}
