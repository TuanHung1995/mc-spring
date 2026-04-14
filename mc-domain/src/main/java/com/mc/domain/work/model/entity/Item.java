package com.mc.domain.work.model.entity;

import com.mc.domain.core.exception.DomainException;
import com.mc.domain.work.model.BaseWorkEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Item — Rich Domain Entity (Work Bounded Context)
 *
 * <p><strong>WHAT IS AN ITEM:</strong>
 * An Item is a row/task within a TaskGroup on a Board. It has a name, a position
 * for drag-and-drop ordering, and a set of ColumnValues (one per Board Column)
 * representing structured field data (status, person, date, etc.).</p>
 *
 * <p>Items can be moved across TaskGroups (e.g., dragged from "To Do" to "Done")
 * — this changes their {@code groupId} reference.</p>
 */
@Getter
public class Item extends BaseWorkEntity {

    // =================================================================
    // STATE
    // =================================================================

    /** The parent Board's ID. */
    private Long boardId;

    /** The current TaskGroup this item belongs to. */
    private Long groupId;

    private String name;

    /**
     * Fractional position for drag-and-drop ordering within a group.
     * Uses gap strategy (65536 base intervals) to allow insertion without renumbering.
     */
    private double position;

    private UUID createdById;
    private UUID updatedById;
    private UUID deletedById;
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private Item() {}

    /** Full-arg reconstitution constructor — persistence mapper only. */
    public Item(Long id, Long boardId, Long groupId, String name, double position,
                UUID createdById, UUID updatedById, UUID deletedById, LocalDateTime deletedAt,
                LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.boardId = boardId;
        this.groupId = groupId;
        this.name = name;
        this.position = position;
        this.createdById = createdById;
        this.updatedById = updatedById;
        this.deletedById = deletedById;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new Item within a TaskGroup.
     *
     * @param boardId     Parent board's ID.
     * @param groupId     Parent group's ID.
     * @param name        The item's display name.
     * @param position    Fractional position (append to end of group).
     * @param createdById ID of the creating user.
     */
    public static Item create(Long boardId, Long groupId, String name,
                              double position, UUID createdById) {
        if (boardId == null || groupId == null) {
            throw new DomainException("Item requires both boardId and groupId");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException("Item name cannot be blank");
        }

        Item item = new Item();
        item.initNew(null);
        item.boardId = boardId;
        item.groupId = groupId;
        item.name = name.trim();
        item.position = position;
        item.createdById = createdById;
        return item;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /** Renames the item. */
    public void rename(String newName, UUID updatedById) {
        if (newName == null || newName.isBlank()) {
            throw new DomainException("Item name cannot be blank");
        }
        this.name = newName.trim();
        this.updatedById = updatedById;
        touch();
    }

    /**
     * Moves this item to a new position, optionally within a different TaskGroup.
     *
     * <p>This is called during drag-and-drop reorder. If {@code newGroupId} differs
     * from {@code groupId}, the item is being moved across groups (a cross-group move).</p>
     *
     * @param newPosition The new fractional position.
     * @param newGroupId  The target group's ID (maybe same group for in-group reorder).
     */
    public void moveTo(double newPosition, Long newGroupId, UUID updatedById) {
        this.position = newPosition;
        if (newGroupId != null) {
            this.groupId = newGroupId;
        }

        this.updatedById = updatedById;
        touch();
    }

    /** Soft-deletes this item. */
    public void trash(UUID deletedById) {
        if (this.isDeleted()) {
            throw new DomainException("Item is already deleted");
        }
        this.deletedById = deletedById;
        this.deletedAt = LocalDateTime.now();
        markDeleted();
    }
}
