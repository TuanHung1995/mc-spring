package com.mc.domain.iam.model;

import com.mc.domain.exception.DomainException;
import lombok.Getter;

import java.time.Instant;

/**
 * TokenBlacklist Domain Model - Pure Domain (no JPA dependencies)
 * Represents a blacklisted/revoked token (e.g., after logout).
 */
@Getter
public class TokenBlacklist {
    
    private Long id;
    private String token;
    private Instant expiryDate;
    private Instant createdAt;

    // =================================================================
    // CONSTRUCTOR (For Persistence)
    // =================================================================
    public TokenBlacklist(Long id, String token, Instant expiryDate, Instant createdAt) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    private TokenBlacklist() {}

    // =================================================================
    // FACTORY METHODS (Creation)
    // =================================================================
    public static TokenBlacklist create(String token, Instant expiryDate) {
        if (token == null || token.isBlank()) {
            throw new DomainException("Token cannot be empty");
        }
        if (expiryDate == null) {
            throw new DomainException("Expiry date cannot be null");
        }
        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.token = token;
        blacklist.expiryDate = expiryDate;
        blacklist.createdAt = Instant.now();
        return blacklist;
    }

    // =================================================================
    // BUSINESS LOGIC (Behavior)
    // =================================================================
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public boolean canBeCleanedUp() {
        // Can be removed from database if expired
        return isExpired();
    }
}
