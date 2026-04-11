package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * CreateItemRequest — Application DTO (Work Context)
 */
@Data
public class CreateItemRequest {

    @NotNull(message = "boardId is required")
    private Long boardId;

    @NotNull(message = "groupId is required")
    private Long groupId;

    @NotBlank(message = "name is required")
    private String name;
}
