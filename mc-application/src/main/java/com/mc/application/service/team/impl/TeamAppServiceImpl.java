package com.mc.application.service.team.impl;

import com.mc.application.mapper.ApartmentMapper;
import com.mc.application.mapper.UserMapper;
import com.mc.application.model.team.*;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.application.service.team.TeamAppService;
import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.repository.WorkspaceMemberRepository;
import com.mc.domain.service.ApartmentDomainService;
import com.mc.domain.service.RoleDomainService;
import com.mc.domain.service.TeamDomainService;
import com.mc.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class TeamAppServiceImpl implements TeamAppService {

    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;
    private final ApartmentDomainService apartmentDomainService;
    private final RoleDomainService roleDomainService;
    private final WorkspaceMemberRepository workspaceMemberRepository;

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

        List<User> users = userDomainService.findAllByEmailIn(request.getEmails());

        if (users.isEmpty()) {
            throw new BusinessLogicException("Can't find any user match email");
        }

        users.stream()
                .filter(user -> !workspaceMemberRepository.memberExist(user.getId()))
                .forEach(user -> {
                    throw new ResourceNotFoundException("Workspace member", "id", user.getId());
                });

        Apartment apartment = apartmentDomainService.findApartmentById(request.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", request.getApartmentId()));

        return userMapper.toUserProfileResponseList(teamDomainService.addApartmentMember(users, apartment));

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

    @Override
    public AssignApartmentOwnerResponse assignApartmentOwner(AssignApartmentOwnerRequest request) {

        User user = userDomainService.findUserById(request.getUserId());
        Apartment apartment = apartmentDomainService.findApartmentById(request.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", request.getApartmentId()));

        return new AssignApartmentOwnerResponse(
                "Owner assigned",
                        user.getEmail(),
                        user.getJobTitle(),
                teamDomainService.assignApartmentOwner(request.isOwner(), user.getId(), apartment.getId())
        );

    }

    @Override
    public RequestToJoinApartmentResponse requestToJoinApartment(RequestToJoinApartmentRequest request) {

        Long currentUserId = userContextPort.getCurrentUserId();

        teamDomainService.requestToJoinApartment(currentUserId, request.getApartmentId());

        return new RequestToJoinApartmentResponse("Request sent");

    }

}
