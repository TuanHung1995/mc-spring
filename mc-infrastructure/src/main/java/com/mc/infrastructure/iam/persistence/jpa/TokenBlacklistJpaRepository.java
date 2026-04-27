package com.mc.infrastructure.iam.persistence.jpa;

import com.mc.infrastructure.iam.persistence.model.TokenBlacklistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA Repository for TokenBlacklistJpaEntity.
 */
@Repository
public interface TokenBlacklistJpaRepository extends JpaRepository<TokenBlacklistJpaEntity, Long> {

    Optional<TokenBlacklistJpaEntity> findByToken(String token);

    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM TokenBlacklistJpaEntity t WHERE t.expiryDate < :now")
    void deleteExpiredTokens(Instant now);
}
