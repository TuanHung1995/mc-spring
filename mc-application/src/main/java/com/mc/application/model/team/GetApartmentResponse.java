package com.mc.application.model.team;

import com.mc.application.model.user.UserProfileResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetApartmentResponse {

    private String apartmentId;
    private String name;
    private String description;
    private String backgroundUrl;
    private Long ownerId;
    private List<UserProfileResponse> members;

}
