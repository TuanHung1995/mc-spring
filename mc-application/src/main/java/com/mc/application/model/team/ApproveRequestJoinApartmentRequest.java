package com.mc.application.model.team;

import lombok.Data;

@Data
public class ApproveRequestJoinApartmentRequest {

    private Long apartmentId;
    private Long userId;
    private boolean isApprove;

}
