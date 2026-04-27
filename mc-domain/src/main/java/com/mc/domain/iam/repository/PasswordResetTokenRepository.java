package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.PasswordResetToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Password Reset Token Repository - Domain Layer
 */
public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken token);

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserId(UUID userId);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);

    void deleteExpiredTokens();
}
