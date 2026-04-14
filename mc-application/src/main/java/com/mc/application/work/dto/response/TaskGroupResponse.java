package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TaskGroupResponse — Application DTO (Work Context)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroupResponse {
    private Long id;
    private Long boardId;
    private String title;
    private String color;
    private double position;
    private boolean collapsed;
    private boolean archived;
    private LocalDateTime archivedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
