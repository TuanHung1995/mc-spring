package com.mc.domain.iam.model;

import com.mc.domain.exception.DomainException;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * RefreshToken Domain Model - Pure Domain (no JPA dependencies)
 * Represents a refresh token for JWT authentication.
 */
@Getter
public class RefreshToken {
    
    private Long id;
    private String token;
    private UUID userId;
    private Instant expiryDate;
    private Instant createdAt;

    // =================================================================
    // CONSTRUCTOR (For Persistence)
    // =================================================================
    public RefreshToken(Long id, String token, UUID userId, Instant expiryDate, Instant createdAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    private RefreshToken() {}

    // =================================================================
    // FACTORY METHODS (Creation)
    // =================================================================
    public static RefreshToken create(UUID userId, long expiryDurationMs) {
        if (userId == null) {
            throw new DomainException("UserId cannot be null");
        }
        RefreshToken token = new RefreshToken();
        token.token = UUID.randomUUID().toString();
        token.userId = userId;
        token.createdAt = Instant.now();
        token.expiryDate = Instant.now().plusMillis(expiryDurationMs);
        return token;
    }

    // =================================================================
    // BUSINESS LOGIC (Behavior)
    // =================================================================
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public void validate() {
        if (isExpired()) {
            throw new DomainException("Refresh token has expired");
        }
    }

    public RefreshToken refresh(long expiryDurationMs) {
        return RefreshToken.create(this.userId, expiryDurationMs);
    }
}
