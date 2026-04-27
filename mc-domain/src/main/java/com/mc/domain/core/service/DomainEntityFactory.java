package com.mc.domain.core.service;

import java.util.UUID;

/**
 * Domain Entity Factory - Domain Layer
 * Provides factory methods for creating domain entities with proper ID initialization.
 * 
 * <p>Usage example:</p>
 * <pre>
 * public class Board extends BaseDomainEntity {
 *     public static Board create(String name, DomainEntityFactory factory) {
 *         Board board = new Board();
 *         factory.initializeEntity(board);
 *         board.name = name;
 *         return board;
 *     }
 * }
 * </pre>
 */
public interface DomainEntityFactory {

    /**
     * Generate a new unique ID.
     */
    UUID generateId();

    /**
     * Generate a time-ordered ID (better for indexing).
     */
    UUID generateTimeOrderedId();
}
