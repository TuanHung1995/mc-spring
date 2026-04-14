package com.mc.domain.work.model.entity;

import com.mc.domain.work.model.BaseWorkEntity;
import lombok.Getter;

import java.time.LocalDateTime;

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
public class ColumnValue extends BaseWorkEntity {

    // =================================================================
    // STATE
    // =================================================================

    private Long itemId;
    private Long columnId;
    private Long boardId;

    /** Generic value field (used for PERSON: user ID, LINK: URL, etc.) */
    private String value;

    /** Text representation of the value (used for TEXT, STATUS labels, DATE strings). */
    private String textValue;

    /** Hex color code for STATUS-type columns. */
    private String color;

    /** Column type string — mirrors the parent column's type for rendering. */
    private String type;

    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private ColumnValue() {}

    /** Full-arg reconstitution constructor — persistence mapper only. */
    public ColumnValue(Long id, Long itemId, Long columnId, Long boardId,
                       String value, String textValue, String color, String type,
                       LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.itemId = itemId;
        this.columnId = columnId;
        this.boardId = boardId;
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
    public static ColumnValue createDefault(Long itemId, Long columnId, Long boardId,
                                            String value, String textValue,
                                            String color, String type) {
        ColumnValue cv = new ColumnValue();
        cv.initNew(null);
        cv.itemId = itemId;
        cv.columnId = columnId;
        cv.boardId = boardId;
        cv.value = value;
        cv.textValue = textValue;
        cv.color = color;
        cv.type = type;
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
        touch();
    }
}
