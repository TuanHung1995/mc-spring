package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TaskGroupResponse — Application DTO (Work Context)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroupResponse {
    private UUID id;
    private UUID boardId;
    private String title;
    private String color;
    private double position;
    private boolean collapsed;
    private boolean archived;
    private LocalDateTime archivedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
