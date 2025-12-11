package com.mc.application.service.team.impl;

import com.mc.application.mapper.ApartmentMapper;
import com.mc.application.model.team.CreateApartmentRequest;
import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.application.service.team.TeamAppService;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Team;
import com.mc.domain.model.entity.User;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.TeamDomainService;
import com.mc.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamAppServiceImpl implements TeamAppService {

    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;

    private final UserContextPort userContextPort;
    private final ApartmentMapper apartmentMapper;

    @Override
    public CreateApartmentResponse createApartment(CreateApartmentRequest request) {

        User currentUser = userDomainService.findUserById(userContextPort.getCurrentUserId());

        Team team = teamDomainService.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace", "id", request.getTeamId()));;

        return apartmentMapper.toCreateApartmentResponse(
                teamDomainService.saveApartment(
                    currentUser,
                    team,
                    request.getName(),
                    request.getDescription(),
                    request.getBackgroundUrl()
                )
        );
    }

}
