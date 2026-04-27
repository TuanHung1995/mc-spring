package com.mc.domain.work.model.entity;

import com.mc.domain.core.exception.DomainException;
import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.work.model.BaseWorkEntity;
import com.mc.domain.work.model.enums.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Board — Rich Domain Entity (Work Bounded Context)
 *
 * <p><strong>WHAT IS A BOARD:</strong>
 * A Board is the primary work surface in Monday Clone. It belongs to a Workspace,
 * has columns (field types), task-groups (row groups), and items (rows/tasks).
 * A Board is the Aggregate Root of the Work context.</p>
 *
 * <p><strong>ID STRATEGY:</strong>
 * Uses {@code Long} (DB AUTO_INCREMENT) because the {@code work_board} table was
 * created with {@code BIGINT AUTO_INCREMENT}. UUID migration is deferred.</p>
 *
 * <p><strong>NO JPA ANNOTATIONS — BY DESIGN.</strong>
 * Persistence is handled by {@code BoardJpaEntity} in mc-infrastructure.</p>
 */
@Getter
public class Board extends BaseDomainEntity {

    // =================================================================
    // STATE
    // =================================================================

    private String name;
    private String description;
    private BoardType type;
    private String purpose;

    /** References the legacy workspace (Long FK). */
    private UUID workspaceId;

    /** References the IAM user who created the board (Long FK → legacy users table). */
    private UUID createdById;

    private UUID deletedById;
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private Board() {}

    /**
     * Reconstitution constructor — persistence mapper only.
     */
    public Board(UUID id, String name, String description, BoardType type, String purpose,
                 UUID workspaceId, UUID createdById, UUID deletedById, LocalDateTime deletedAt,
                 LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.name = name;
        this.description = description;
        this.type = type;
        this.purpose = purpose;
        this.workspaceId = workspaceId;
        this.createdById = createdById;
        this.deletedById = deletedById;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new Board.
     *
     * <p>The ID is null at creation — it will be assigned by the DB on INSERT.
     * This is the standard pattern for AUTO_INCREMENT entities.</p>
     *
     * @param name        Display name of the board.
     * @param type        Board type (BOARD, PRIVATE, SHAREABLE).
     * @param purpose     The column header label for the main "name" column.
     * @param createdById The Long user-id of the creator (from legacy session).
     * @param workspaceId The Long ID of the parent Workspace.
     */
    public static Board create(String name, BoardType type, String purpose,
                               UUID createdById, UUID workspaceId) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Board name cannot be blank");
        }
        if (createdById == null) {
            throw new DomainException("Board creator cannot be null");
        }
        if (workspaceId == null) {
            throw new DomainException("Board must belong to a Workspace");
        }

        Board board = new Board();
        board.initializeNewEntity(IdUtils.newId());
        board.name = name.trim();
        board.type = type != null ? type : BoardType.PUBLIC;
        board.purpose = purpose;
        board.createdById = createdById;
        board.workspaceId = workspaceId;
        return board;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /**
     * Renames the board.
     */
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new DomainException("Board name cannot be blank");
        }
        this.name = newName.trim();
        markAsModified();
    }

    /**
     * Soft-deletes (trashes) this board.
     *
     * @param deletedById ID of the user performing the deletion.
     */
    public void trash(UUID deletedById) {
        if (this.isDeleted()) {
            throw new DomainException("Board is already deleted");
        }
        this.deletedById = deletedById;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted();
    }
}
