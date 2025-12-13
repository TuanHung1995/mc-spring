package com.mc.application.model.team;

import lombok.Data;

@Data
public class AddApartmentMemberRequest {

    private String email;
    private Long apartmentId;
    private Long workspaceId;

}
