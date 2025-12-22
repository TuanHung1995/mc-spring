package com.mc.application.model.team;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignApartmentOwnerResponse {

    private String message;
    private String email;
    private String jobTitle;
    private boolean isOwner;

}
