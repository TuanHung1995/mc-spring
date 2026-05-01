package com.mc.domain.work.model.entity;

import com.mc.domain.core.exception.DomainException;
import com.mc.domain.work.model.BaseWorkEntity;
import com.mc.domain.work.model.enums.BoardColumnType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BoardColumn — Rich Domain Entity (Work Bounded Context)
 *
 * <p><strong>NOTE on naming:</strong>
 * Named {@code BoardColumn} (not {@code Column}) to avoid clash with
 * {@code java.sql.Types} and {@code java.lang} idioms. The DB table is {@code board_columns}.
 * The legacy entity was named {@code Column}, which shadowed java.sql.* in some contexts.</p>
 *
 * <p><strong>WHAT IS A BOARD COLUMN:</strong>
 * A column defines a field type for every row (Item) on a Board.
 * Examples: Status (colored labels), Person (user assignment), Date, Text.
 * Columns can be reordered, hidden, or deleted.</p>
 */
@Getter
public class BoardColumn extends BaseWorkEntity {

    // =================================================================
    // STATE
    // =================================================================

    /** The parent Board's ID. */
    private UUID boardId;

    private String title;
    private BoardColumnType type;
    private String description;
    private double position;
    private int width;
    private boolean hidden;

    private UUID workspaceId;
    private UUID teamId;

    private UUID createdById;
    private UUID updatedById;
    private UUID deletedById;
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private BoardColumn() {}

    /** Full-arg reconstitution constructor — persistence mapper only. */
    public BoardColumn(Long id, UUID boardId, String title, BoardColumnType type, String description,
                       double position, int width, boolean hidden,
                       UUID workspaceId, UUID teamId,
                       UUID createdById, UUID updatedById, UUID deletedById, LocalDateTime deletedAt,
                       LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.boardId = boardId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.position = position;
        this.width = width;
        this.hidden = hidden;
        this.workspaceId = workspaceId;
        this.teamId = teamId;
        this.createdById = createdById;
        this.updatedById = updatedById;
        this.deletedById = deletedById;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new Board Column.
     *
     * @param boardId     Parent board's ID.
     * @param title       Column header label.
     * @param type        Field type (TEXT, STATUS, PERSON, DATE, etc.).
     * @param position    Fractional position for ordering.
     * @param createdById ID of the user creating the column.
     */
    public static BoardColumn create(UUID boardId, String title, BoardColumnType type,
                                     double position, UUID createdById, UUID workspaceId, UUID teamId) {
        if (boardId == null) {
            throw new DomainException("BoardColumn must belong to a Board");
        }
        if (title == null || title.isBlank()) {
            throw new DomainException("BoardColumn title cannot be blank");
        }
        if (type == null) {
            throw new DomainException("BoardColumn type cannot be null");
        }

        BoardColumn col = new BoardColumn();
        col.initNew(null);
        col.boardId = boardId;
        col.title = title.trim();
        col.type = type;
        col.position = position;
        col.width = 150; // default width in pixels
        col.hidden = false;
        col.createdById = createdById;
        col.workspaceId = workspaceId;
        col.teamId = teamId;
        return col;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /** Renames the column. */
    public void rename(String newTitle, UUID updatedById) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new DomainException("Column title cannot be blank");
        }
        this.title = newTitle.trim();
        this.updatedById = updatedById;
        touch();
    }

    /** Moves this column to a new drag-and-drop position. */
    public void moveTo(double newPosition, UUID updatedById) {
        this.position = newPosition;
        this.updatedById = updatedById;
        touch();
    }

    /** Toggles column visibility. */
    public void toggleHidden() {
        this.hidden = !this.hidden;
        touch();
    }

    /** Resizes the column. */
    public void resize(int newWidth) {
        if (newWidth < 50) {
            throw new DomainException("Column width must be at least 50px");
        }
        this.width = newWidth;
        touch();
    }

    /** Soft-deletes this column. */
    public void trash(UUID deletedById) {
        if (this.isDeleted()) {
            throw new DomainException("Column is already deleted");
        }
        this.deletedById = deletedById;
        this.deletedAt = LocalDateTime.now();
        markDeleted();
    }
}
