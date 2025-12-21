package com.mc.application.model.team;

import lombok.Data;

@Data
public class DeleteApartmentMemberRequest {

    private Long userId;
    private Long apartmentId;
    private Long workspaceId;

}
