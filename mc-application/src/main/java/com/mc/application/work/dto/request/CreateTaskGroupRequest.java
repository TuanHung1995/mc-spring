package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * CreateTaskGroupRequest — Application DTO (Work Context)
 */
@Data
public class CreateTaskGroupRequest {

    @NotNull(message = "boardId is required")
    private UUID boardId;

    @NotNull(message = "workspaceId is required")
    private UUID workspaceId;

    @NotNull(message = "teamId is required")
    private UUID teamId;

    @NotBlank(message = "title is required")
    private String title;

    /** Optional hex accent color (e.g., "#579bfc"). Defaults to blue if absent. */
    private String color;
}
