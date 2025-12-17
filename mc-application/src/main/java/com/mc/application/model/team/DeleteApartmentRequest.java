package com.mc.application.model.team;

import lombok.Data;

@Data
public class DeleteApartmentRequest {

    private Long workspaceId;
    private Long apartmentId;

}
