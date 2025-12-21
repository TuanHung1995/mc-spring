package com.mc.application.model.team;

import lombok.Data;

@Data
public class RequestToJoinApartmentRequest {

    private Long apartmentId;
    private Long workspaceId;

}
