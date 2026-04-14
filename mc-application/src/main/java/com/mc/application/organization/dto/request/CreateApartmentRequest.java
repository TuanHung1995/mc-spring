package com.mc.application.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateApartmentRequest {
    @NotNull
    private UUID teamId;
    
    @NotNull
    private UUID workspaceId;
    
    @NotBlank
    private String name;
    
    private String description;
    private String backgroundUrl;
}
