package com.mc.infrastructure.iam.persistence.jpa;

import com.mc.infrastructure.iam.persistence.model.PasswordResetTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for PasswordResetTokenJpaEntity
 */
@Repository
public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenJpaEntity, UUID> {

    Optional<PasswordResetTokenJpaEntity> findByToken(String token);

    Optional<PasswordResetTokenJpaEntity> findByUserId(UUID userId);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM PasswordResetTokenJpaEntity p WHERE p.expiryDate < :now")
    void deleteExpiredTokens(Instant now);
}
