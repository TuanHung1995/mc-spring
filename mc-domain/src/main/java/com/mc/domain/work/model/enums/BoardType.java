package com.mc.domain.work.model.enums;

/**
 * BoardType — Domain Enum (Work Context)
 *
 * <p>Defines the structural type of a Board.
 * BOARD is the standard project board view.
 * PRIVATE boards are hidden from non-members.</p>
 */
public enum BoardType {
    PUBLIC,
    PRIVATE,
    SHAREABLE
}
