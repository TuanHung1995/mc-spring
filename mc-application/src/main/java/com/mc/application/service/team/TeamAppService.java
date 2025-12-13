package com.mc.application.service.team;

import com.mc.application.model.team.AddApartmentMemberRequest;
import com.mc.application.model.team.CreateApartmentRequest;
import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.application.model.user.UserProfileResponse;

import java.util.List;

public interface TeamAppService {

    List<UserProfileResponse> addApartmentMember(AddApartmentMemberRequest request);

    CreateApartmentResponse createApartment(CreateApartmentRequest createApartmentRequest);

}
