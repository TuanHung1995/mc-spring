package com.mc.infrastructure.persistence.id;

import com.mc.domain.core.service.IdGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * UUID Generator Implementation - Infrastructure Layer
 * Implements IdGenerator with support for both random and time-ordered UUIDs.
 * 
 * <p>Strategies:</p>
 * <ul>
 *   <li><b>generate()</b>: UUID v4 (random) - Best for general use</li>
 *   <li><b>generateTimeOrdered()</b>: UUID v7-like (time-ordered) - Better for DB indexing</li>
 * </ul>
 */
@Component
public class UuidGenerator implements IdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }

    /**
     * Generate a time-ordered UUID (similar to UUID v7).
     * Format: [48-bit timestamp][4-bit version][12-bit random][2-bit variant][62-bit random]
     * 
     * <p>Benefits:</p>
     * <ul>
     *   <li>Better B-tree index performance (sequential inserts)</li>
     *   <li>Natural ordering by creation time</li>
     *   <li>Still globally unique</li>
     * </ul>
     */
    @Override
    public UUID generateTimeOrdered() {
        long timestamp = Instant.now().toEpochMilli();
        
        // Build most significant bits: timestamp + version (7)
        long msb = (timestamp << 16) | 0x7000L | (RANDOM.nextLong() & 0x0FFFL);
        
        // Build least significant bits: variant (2) + random
        long lsb = (0x8000000000000000L) | (RANDOM.nextLong() & 0x3FFFFFFFFFFFFFFFL);
        
        return new UUID(msb, lsb);
    }

    /**
     * Static helper for quick UUID generation without DI.
     * Use sparingly - prefer injecting IdGenerator for testability.
     */
    public static UUID quickGenerate() {
        return UUID.randomUUID();
    }

    /**
     * Static helper for quick time-ordered UUID generation.
     */
    public static UUID quickGenerateTimeOrdered() {
        return new UuidGenerator().generateTimeOrdered();
    }
}
