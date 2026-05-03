package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * CreateBoardRequest — Application DTO (Work Context)
 */
@Data
public class CreateBoardRequest {

    @NotNull(message = "workspaceId is required")
    private UUID workspaceId;

    @NotNull(message = "teamId is required")
    private UUID teamId;

    @NotBlank(message = "name is required")
    private String name;

    /** Optional short description for the main column header (e.g., "Task", "Project"). */
    private String purpose;

    /**
     * Board visibility type: BOARD, PRIVATE, or SHAREABLE.
     * Defaults to BOARD if not provided.
     */
    private String type;
}
