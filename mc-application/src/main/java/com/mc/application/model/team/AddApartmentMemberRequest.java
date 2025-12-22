package com.mc.application.model.team;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddApartmentMemberRequest {

    @NotNull(message = "Email is required")
    private List<String> emails;

    @NotNull(message = "Apartment id is required")
    private Long apartmentId;
    private Long workspaceId;

}
