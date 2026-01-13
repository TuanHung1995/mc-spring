package com.mc.domain.iam.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: Raised when a new user registers in the system.
 */
public record UserRegisteredEvent(
    UUID userId,
    String email,
    String fullName,
    Instant occurredAt
) {
    public static UserRegisteredEvent of(UUID userId, String email, String fullName) {
        return new UserRegisteredEvent(userId, email, fullName, Instant.now());
    }
}
