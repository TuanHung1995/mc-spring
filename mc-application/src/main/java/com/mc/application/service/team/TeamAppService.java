package com.mc.application.service.team;

import com.mc.application.model.team.*;
import com.mc.application.model.user.UserProfileResponse;

import java.util.List;

public interface TeamAppService {

    List<UserProfileResponse> addApartmentMember(AddApartmentMemberRequest request);

    CreateApartmentResponse createApartment(CreateApartmentRequest createApartmentRequest);

    DeleteApartmentResponse deleteApartmentById(Long id);

    List<UserProfileResponse> deleteApartmentMember(DeleteApartmentMemberRequest request);

    AssignApartmentOwnerResponse assignApartmentOwner(AssignApartmentOwnerRequest request);

}
