package com.mc.application.service.team;

import com.mc.application.model.team.CreateApartmentRequest;
import com.mc.application.model.team.CreateApartmentResponse;

public interface TeamAppService {

    CreateApartmentResponse createApartment(CreateApartmentRequest createApartmentRequest);

}
