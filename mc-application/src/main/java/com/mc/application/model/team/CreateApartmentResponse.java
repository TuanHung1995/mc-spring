package com.mc.application.model.team;

import lombok.Data;

@Data
public class CreateApartmentResponse {

    private Long ownerId;
    private String name;
    private String description;

}
