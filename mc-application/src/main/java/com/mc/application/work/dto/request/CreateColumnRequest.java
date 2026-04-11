package com.mc.application.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * CreateColumnRequest — Application DTO (Work Context)
 */
@Data
public class CreateColumnRequest {

    @NotNull(message = "boardId is required")
    private Long boardId;

    @NotBlank(message = "title is required")
    private String title;

    /**
     * Column type: TEXT, PERSON, STATUS, DATE, NUMBER, CHECKBOX, LINK.
     * Defaults to TEXT if not provided.
     */
    private String type;
}
