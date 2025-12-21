package com.mc.application.model.team;

import lombok.Data;

@Data
public class AssignApartmentOwnerRequest {

    private Long userId;
    private Long apartmentId;
    private Long workspaceId;
    private boolean isOwner;

}
