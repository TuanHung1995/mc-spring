package com.mc.domain.iam.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: Raised when a user account is locked due to too many failed login attempts.
 */
public record AccountLockedEvent(
    UUID userId,
    String email,
    int failedAttempts,
    Instant occurredAt
) {
    public static AccountLockedEvent of(UUID userId, String email, int failedAttempts) {
        return new AccountLockedEvent(userId, email, failedAttempts, Instant.now());
    }
}
