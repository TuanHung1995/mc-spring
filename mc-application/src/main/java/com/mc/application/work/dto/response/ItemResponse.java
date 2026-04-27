package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ItemResponse — Application DTO (Work Context)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private UUID id;
    private UUID boardId;
    private UUID groupId;
    private String name;
    private double position;
    private UUID createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
