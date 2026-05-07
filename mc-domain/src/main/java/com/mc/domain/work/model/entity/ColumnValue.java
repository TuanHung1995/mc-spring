package com.mc.domain.work.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * ColumnValue — Domain Entity (Work Bounded Context)
 *
 * <p><strong>WHAT IS A COLUMN VALUE:</strong>
 * A ColumnValue stores the data for one Cell — the intersection of an Item (row) and
 * a BoardColumn (column). Every Item has exactly one ColumnValue per BoardColumn.
 * They are created automatically when an Item is created (one per non-primary column).</p>
 *
 * <p>The {@code type} field mirrors the column type (TEXT, STATUS, PERSON, DATE) and
 * controls how the value is rendered and stored. Some types use {@code textValue}
 * (strings), some use {@code color} (for STATUS labels).</p>
 */
@Getter
public class ColumnValue extends BaseDomainEntity {

    // =================================================================
    // STATE
    // =================================================================

    private UUID itemId;
    private Long columnId;
    private UUID boardId;
    private UUID taskGroupId;
    private UUID workspaceId;
    private UUID teamId;

    /** Generic value field (used for PERSON: user ID, LINK: URL, etc.) */
    private String value;

    /** Text representation of the value (used for TEXT, STATUS labels, DATE strings). */
    private String textValue;

    /** Hex color code for STATUS-type columns. */
    private String color;

    /** Column type string — mirrors the parent column's type for rendering. */
    private String type;

    private LocalDateTime deletedAt;
    private UUID deletedBy;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private ColumnValue() {}

    /** Full-arg reconstitution constructor — persistence mapper only. */
    public ColumnValue(UUID id, UUID itemId, Long columnId, UUID boardId,
                       UUID taskGroupId, UUID workspaceId, UUID teamId,
                       String value, String textValue, String color, String type,
                       LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.itemId = itemId;
        this.columnId = columnId;
        this.boardId = boardId;
        this.taskGroupId = taskGroupId;
        this.workspaceId = workspaceId;
        this.teamId = teamId;
        this.value = value;
        this.textValue = textValue;
        this.color = color;
        this.type = type;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates the initial ColumnValue when an Item is first inserted.
     *
     * @param itemId     Parent item's ID.
     * @param columnId   Parent column's ID.
     * @param boardId    Parent board's ID (denormalized for query performance).
     * @param value      Generic value (nullable).
     * @param textValue  Text display value (nullable).
     * @param color      Color hex (nullable; only for STATUS columns).
     * @param type       Column type string matching {@code BoardColumnType}).
     */
    public static ColumnValue createDefault(UUID itemId, Long columnId, UUID boardId,
                                            UUID taskGroupId, UUID workspaceId, UUID teamId,
                                            String value, String textValue,
                                            String color, String type) {
        ColumnValue cv = new ColumnValue();
        cv.initializeNewEntity(IdUtils.newId());
        cv.itemId = itemId;
        cv.columnId = columnId;
        cv.boardId = boardId;
        cv.taskGroupId = taskGroupId;
        cv.workspaceId = workspaceId;
        cv.teamId = teamId;
        cv.value = value;
        cv.textValue = textValue;
        cv.color = color;
        cv.type = type;
        return cv;
    }

    public static ColumnValue createBaseOnColumn(UUID itemId, Long columnId, UUID boardId,
                                            UUID taskGroupId, UUID workspaceId, UUID teamId, String type) {
        ColumnValue cv = new ColumnValue();
        cv.initializeNewEntity(IdUtils.newId());
        cv.itemId = itemId;
        cv.columnId = columnId;
        cv.boardId = boardId;
        cv.taskGroupId = taskGroupId;
        cv.workspaceId = workspaceId;
        cv.teamId = teamId;
        cv.type = type;
        switch (type) {
            case "TEXT":
                cv.textValue = "TEXT";
                break;
            case "STATUS":
                cv.value = "NORMAL";
                break;
        }
        return cv;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /**
     * Updates the cell value. Only non-null params are applied (partial update).
     */
    public void updateValue(String value, String textValue, String color) {
        if (value != null) this.value = value;
        if (textValue != null) this.textValue = textValue;
        if (color != null) this.color = color;
        markAsModified();
    }

    public void trash(UUID deletedById) {
        this.deletedBy = deletedById;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted();
    }
}
