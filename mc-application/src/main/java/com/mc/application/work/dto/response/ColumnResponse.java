package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ColumnResponse — Application DTO (Work Context)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnResponse {
    private Long id;
    private UUID boardId;
    private String title;
    private String type;
    private String description;
    private double position;
    private int width;
    private boolean hidden;
    private LocalDateTime createdAt;
}
