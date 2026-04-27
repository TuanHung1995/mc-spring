package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * CreateItemRequest — Application DTO (Work Context)
 */
@Data
public class CreateItemRequest {

    @NotNull(message = "boardId is required")
    private UUID boardId;

    @NotNull(message = "groupId is required")
    private UUID groupId;

    @NotBlank(message = "name is required")
    private String name;
}
