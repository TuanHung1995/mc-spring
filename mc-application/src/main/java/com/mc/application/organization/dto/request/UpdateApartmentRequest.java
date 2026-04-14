package com.mc.application.organization.dto.request;

import lombok.Data;

@Data
public class UpdateApartmentRequest {
    private String name;
    private String description;
    private String backgroundUrl;
}
