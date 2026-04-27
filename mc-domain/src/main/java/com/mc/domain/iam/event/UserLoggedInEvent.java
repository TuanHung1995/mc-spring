package com.mc.domain.iam.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: Raised when a user successfully logs in.
 */
public record UserLoggedInEvent(
    UUID userId,
    String email,
    String ipAddress,
    Instant occurredAt
) {
    public static UserLoggedInEvent of(UUID userId, String email, String ipAddress) {
        return new UserLoggedInEvent(userId, email, ipAddress, Instant.now());
    }
}
