package com.mc.application.service.team.impl;

import com.mc.application.mapper.ApartmentMapper;
import com.mc.application.mapper.UserMapper;
import com.mc.application.model.team.*;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.application.service.team.TeamAppService;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.entity.Team;
import com.mc.domain.model.entity.User;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.ApartmentDomainService;
import com.mc.domain.service.TeamDomainService;
import com.mc.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamAppServiceImpl implements TeamAppService {

    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;
    private final ApartmentDomainService apartmentDomainService;

    private final UserContextPort userContextPort;
    private final ApartmentMapper apartmentMapper;
    private final UserMapper userMapper;

    @Override
    public CreateApartmentResponse createApartment(CreateApartmentRequest request) {

        User currentUser = userDomainService.findUserById(userContextPort.getCurrentUserId());

        Team team = teamDomainService.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace", "id", request.getTeamId()));

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

    @Override
    public List<UserProfileResponse> addApartmentMember(AddApartmentMemberRequest request) {

        User member = userDomainService.findUserByEmail(request.getEmail());

        Apartment apartment = apartmentDomainService.findApartmentById(request.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", request.getApartmentId()));

        return userMapper.toUserProfileResponseList(teamDomainService.addApartmentMember(member, apartment));

    }

    @Override
    public DeleteApartmentResponse deleteApartmentById(Long id) {

        Apartment apartment = apartmentDomainService.findApartmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));

        apartmentDomainService.deleteTeam(apartment.getId());

        return new DeleteApartmentResponse("Team deleted successfully");
    }

    @Override
    public List<UserProfileResponse> deleteApartmentMember(DeleteApartmentMemberRequest request) {

        User member = userDomainService.findUserById(request.getUserId());

        Apartment apartment = apartmentDomainService.findApartmentById(request.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", request.getApartmentId()));

        ApartmentMember pivot = apartmentDomainService.findApartmentMemberByUserIdAndApartmentId(member.getId(), apartment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment Member", "userId", member.getId()));

        return userMapper.toUserProfileResponseList(teamDomainService.deletedApartmentMember(pivot));

    }

}
