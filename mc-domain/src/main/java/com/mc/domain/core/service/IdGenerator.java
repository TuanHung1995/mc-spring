package com.mc.domain.core.service;

import java.util.UUID;

/**
 * ID Generator Service Interface - Domain Layer
 * Provides ID generation strategies for domain entities.
 * 
 * <p>This abstraction allows:</p>
 * <ul>
 *   <li>Different ID strategies (UUID v4, UUID v7, ULID, Snowflake)</li>
 *   <li>Testability (can mock for deterministic tests)</li>
 *   <li>Centralized ID generation logic</li>
 * </ul>
 */
public interface IdGenerator {

    /**
     * Generate a new unique identifier.
     * @return A new UUID
     */
    UUID generate();

    /**
     * Generate a time-based unique identifier.
     * Useful for entities that benefit from time-ordered IDs.
     * @return A new time-based UUID
     */
    UUID generateTimeOrdered();
}
