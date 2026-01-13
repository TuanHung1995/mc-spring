package com.mc.domain.core.util;

import java.util.UUID;

/**
 * ID Utility - Domain Layer
 * Static utility for simple ID generation without dependency injection.
 * 
 * <p>Use cases:</p>
 * <ul>
 *   <li>Domain entity factory methods</li>
 *   <li>Test fixtures</li>
 *   <li>Quick prototyping</li>
 * </ul>
 * 
 * <p>For production code requiring testability, prefer injecting {@code IdGenerator}.</p>
 */
public final class IdUtils {

    private IdUtils() {
        // Utility class - no instantiation
    }

    /**
     * Generate a random UUID (v4).
     * Good for most use cases.
     */
    public static UUID newId() {
        return UUID.randomUUID();
    }

    /**
     * Generate a time-ordered UUID (v7-like).
     * Better for database index performance.
     */
    public static UUID newTimeOrderedId() {
        long timestamp = System.currentTimeMillis();
        
        // Build most significant bits: timestamp + version (7)
        long msb = (timestamp << 16) | 0x7000L | (randomBits() & 0x0FFFL);
        
        // Build the least significant bits: variant (2) + random
        long lsb = (0x8000000000000000L) | (randomBits() & 0x3FFFFFFFFFFFFFFFL);
        
        return new UUID(msb, lsb);
    }

    /**
     * Parse UUID from string, returning null if invalid.
     */
    public static UUID parseOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Check if a string is a valid UUID format.
     */
    public static boolean isValidUuid(String value) {
        return parseOrNull(value) != null;
    }

    private static long randomBits() {
        return java.util.concurrent.ThreadLocalRandom.current().nextLong();
    }
}
