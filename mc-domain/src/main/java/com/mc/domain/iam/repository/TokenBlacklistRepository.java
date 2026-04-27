package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.TokenBlacklist;

import java.util.Optional;

/**
 * TokenBlacklist Repository Interface - Domain Layer
 * Defines persistence operations for TokenBlacklist entity.
 */
public interface TokenBlacklistRepository {

    TokenBlacklist save(TokenBlacklist tokenBlacklist);

    Optional<TokenBlacklist> findByToken(String token);

    boolean existsByToken(String token);

    void deleteExpiredTokens();
}
