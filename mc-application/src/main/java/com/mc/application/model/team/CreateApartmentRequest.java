package com.mc.application.model.team;

import lombok.Data;

@Data
public class CreateApartmentRequest {

    private Long teamId;
    private Long ownerId;
    private String name;
    private String description;
    private String backgroundUrl;

}
