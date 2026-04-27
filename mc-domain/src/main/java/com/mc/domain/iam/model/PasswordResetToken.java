package com.mc.domain.iam.model;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.iam.exception.TokenExpiredException;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Password Reset Token Domain Model - IAM Bounded Context
 * Represents a token for password reset functionality.
 */
@Getter
public class PasswordResetToken extends BaseDomainEntity {

    private String token;
    private UUID userId;
    private Instant expiryDate;
    private boolean used;

    // =================================================================
    // CONSTRUCTOR (For Persistence)
    // =================================================================
    public PasswordResetToken(UUID id, String token, UUID userId, Instant expiryDate,
                               boolean used, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.token = token;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.used = used;
    }

    private PasswordResetToken() {
        super();
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================
    
    /**
     * Create a new password reset token.
     * @param userId User ID for whom the token is created
     * @param expirationMs Token validity duration in milliseconds
     * @return New PasswordResetToken
     */
    public static PasswordResetToken create(UUID userId, long expirationMs) {
        PasswordResetToken token = new PasswordResetToken();
        token.initializeNewEntity(IdUtils.newId());
        token.token = UUID.randomUUID().toString();
        token.userId = userId;
        token.expiryDate = Instant.now().plusMillis(expirationMs);
        token.used = false;
        return token;
    }

    // =================================================================
    // BUSINESS LOGIC
    // =================================================================
    
    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public void validate() {
        if (isExpired()) {
            throw new TokenExpiredException("Password reset token has expired");
        }
        if (used) {
            throw new TokenExpiredException("Password reset token has already been used");
        }
    }

    public void markAsUsed() {
        this.used = true;
        markAsModified();
    }
}
