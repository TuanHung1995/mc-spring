package com.mc.infrastructure.iam.persistence.jpa;

import com.mc.infrastructure.iam.persistence.model.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for UserJpaEntity.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    @Query("SELECT u FROM UserJpaEntity u WHERE " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserJpaEntity> searchUsers(@Param("keyword") String keyword);

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
