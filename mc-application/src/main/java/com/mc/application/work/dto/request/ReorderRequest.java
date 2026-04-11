package com.mc.application.work.dto.request;

import lombok.Data;

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
    private Long targetId;
    private Long previousId;
    private Long nextId;
    /** Only populated for item reorder — indicates the target group after a cross-group move. */
    private Long targetGroupId;
}
