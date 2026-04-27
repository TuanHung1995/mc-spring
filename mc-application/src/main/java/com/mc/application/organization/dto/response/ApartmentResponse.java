package com.mc.application.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentResponse {
    private UUID id;
    private String name;
    private String description;
    private String backgroundUrl;
    private UUID ownerId;
    private UUID teamId;
    private UUID workspaceId;
}
