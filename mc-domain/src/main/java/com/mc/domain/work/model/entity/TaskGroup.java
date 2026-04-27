package com.mc.domain.work.model.entity;

import com.mc.domain.core.exception.DomainException;
import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.work.model.BaseWorkEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TaskGroup — Rich Domain Entity (Work Bounded Context)
 *
 * <p><strong>WHAT IS A TASK GROUP:</strong>
 * A TaskGroup is a row-group (section) within a Board. Each group has a colored header
 * and contains Items (rows/tasks). Groups can be reordered (position), archived, or trashed.</p>
 *
 * <p><strong>LIFECYCLE:</strong>
 * normal → archived (hidden from main view, recoverable)
 * normal → trashed/soft-deleted (in trash, recoverable)
 * trashed → permanently deleted (irreversible)</p>
 */
@Getter
public class TaskGroup extends BaseDomainEntity {

    // =================================================================
    // STATE
    // =================================================================

    /** The parent Board's Long ID. */
    private UUID boardId;

    private String title;
    private String color;

    /**
     * Fractional position for drag-and-drop ordering.
     * Uses a gap strategy (e.g., 65536 base) to allow insertion between items
     * without renumbering all positions.
     */
    private double position;

    private boolean collapsed;
    private boolean archived;
    private LocalDateTime archivedAt;
    private UUID archivedBy;
    private UUID createdBy;
    private UUID updatedBy;
    private UUID deletedBy;
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private TaskGroup() {}

    /** Full-arg reconstitution constructor — persistence mapper only. */
    public TaskGroup(UUID id, UUID boardId, String title, String color, double position,
                     boolean collapsed, boolean archived, LocalDateTime archivedAt, UUID archivedBy,
                     UUID createdBy, UUID updatedBy, UUID deletedBy, LocalDateTime deletedAt,
                     LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.boardId = boardId;
        this.title = title;
        this.color = color;
        this.position = position;
        this.collapsed = collapsed;
        this.archived = archived;
        this.archivedAt = archivedAt;
        this.archivedBy = archivedBy;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new TaskGroup appended to a Board.
     *
     * @param boardId   The parent board's ID.
     * @param title     The group header label.
     * @param color     Hex color code (e.g., "#579bfc"). Defaults to blue if null.
     * @param position  The fractional position (calculated by the service).
     * @param createdBy The user creating this group.
     */
    public static TaskGroup create(UUID boardId, String title, String color,
                                   double position, UUID createdBy) {
        if (boardId == null) {
            throw new DomainException("TaskGroup must belong to a Board");
        }
        if (title == null || title.isBlank()) {
            throw new DomainException("TaskGroup title cannot be blank");
        }

        TaskGroup group = new TaskGroup();
        group.initializeNewEntity(IdUtils.newId());
        group.boardId = boardId;
        group.title = title.trim();
        group.color = (color != null && !color.isBlank()) ? color : "#579bfc";
        group.position = position;
        group.collapsed = false;
        group.archived = false;
        group.createdBy = createdBy;
        return group;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /** Updates the group's header label and/or accent color. */
    public void update(String newTitle, String newColor, UUID updatedBy) {
        if (newTitle != null && !newTitle.isBlank()) {
            this.title = newTitle.trim();
        }
        if (newColor != null && !newColor.isBlank()) {
            this.color = newColor;
        }

        this.updatedBy = updatedBy;
        markAsModified();
    }

    /** Moves this group to a new drag-and-drop position. */
    public void moveTo(double newPosition, UUID updatedBy) {
        this.position = newPosition;
        this.updatedBy = updatedBy;
        markAsModified();
    }

    /** Archives this group (hides it from the main board view). */
    public void archive(UUID archivedBy) {
        if (this.archived) {
            throw new DomainException("TaskGroup is already archived");
        }
        this.archived = true;
        this.archivedAt = LocalDateTime.now();
        this.archivedBy = archivedBy;
        markAsModified();
    }

    /** Unarchives this group, making it visible again. */
    public void unarchive() {
        if (!this.archived) {
            throw new DomainException("TaskGroup is not archived");
        }
        this.archived = false;
        this.archivedAt = null;
        this.archivedBy = null;
        markAsModified();
    }

    /** Soft-deletes (trashes) this group. */
    public void trash(UUID deletedBy) {
        if (this.isDeleted()) {
            throw new DomainException("TaskGroup is already in trash");
        }
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted();
    }

    /** Restores a trashed group back to active state. */
    @Override
    public void restore() {
        this.deletedBy = null;
        this.deletedAt = null;
        super.restore();
    }
}
