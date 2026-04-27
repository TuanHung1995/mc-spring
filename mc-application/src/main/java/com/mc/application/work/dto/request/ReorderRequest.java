package com.mc.application.work.dto.request;

import lombok.Data;

import java.util.UUID;

/**
 * ReorderRequest — Application DTO (Work Context)
 *
 * <p>Used for all three drag-and-drop reorder operations:
 * Group reorder, Column reorder, and Item reorder.</p>
 *
 * <ul>
 *   <li>{@code targetId} — ID of the entity being dragged</li>
 *   <li>{@code previousId} — ID of the entity immediately before the new position (null = top)</li>
 *   <li>{@code nextId} — ID of the entity immediately after the new position (null = bottom)</li>
 *   <li>{@code targetGroupId} — only used for Item reorder (cross-group move support)</li>
 * </ul>
 */
@Data
public class ReorderRequest {
    private UUID targetId;
    private UUID previousId;
    private UUID nextId;
    /** Only populated for item reorder — indicates the target group after a cross-group move. */
    private UUID targetGroupId;
}
