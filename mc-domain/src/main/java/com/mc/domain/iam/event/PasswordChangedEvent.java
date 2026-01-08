package com.mc.domain.iam.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: Raised when a user changes their password.
 */
public record PasswordChangedEvent(
    UUID userId,
    String email,
    Instant occurredAt
) {
    public static PasswordChangedEvent of(UUID userId, String email) {
        return new PasswordChangedEvent(userId, email, Instant.now());
    }
}
