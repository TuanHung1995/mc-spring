package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateWorkspaceRequest {
    @NotNull
    private UUID teamId;

    @NotBlank
    private String name;
}
