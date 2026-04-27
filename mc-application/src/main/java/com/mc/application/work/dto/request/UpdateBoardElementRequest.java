package com.mc.application.work.dto.request;

import lombok.Data;

import java.util.UUID;

/**
 * UpdateBoardElementRequest — Application DTO (Work Context)
 *
 * <p>Multiplex DTO used by the inline-edit endpoint to update a single
 * field on any board element (group title, column title, item name).
 * The {@code type} field discriminates which entity to update.</p>
 */
@Data
public class UpdateBoardElementRequest {
    /** Discriminator: "TASK_GROUP" | "COLUMN" | "ITEM" */
    private String type;
    /** ID of the entity to update. */
    private UUID targetId;
    /** The new value (e.g., new title or name). */
    private String value;
    /** Optional: new color (only used for TASK_GROUP). */
    private String color;
}
