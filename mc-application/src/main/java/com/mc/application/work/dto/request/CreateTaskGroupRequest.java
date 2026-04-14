package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * CreateTaskGroupRequest — Application DTO (Work Context)
 */
@Data
public class CreateTaskGroupRequest {

    @NotNull(message = "boardId is required")
    private Long boardId;

    @NotBlank(message = "title is required")
    private String title;

    /** Optional hex accent color (e.g., "#579bfc"). Defaults to blue if absent. */
    private String color;
}
