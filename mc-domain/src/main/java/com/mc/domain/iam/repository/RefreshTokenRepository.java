package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

/**
 * RefreshToken Repository Interface - Domain Layer
 * Defines persistence operations for RefreshToken entity.
 */
public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(UUID userId);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    void deleteExpiredTokens();
}
