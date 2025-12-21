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

    RequestToJoinApartmentResponse requestToJoinApartment(RequestToJoinApartmentRequest request);

    void approveRequestJoinApartment(ApproveRequestJoinApartmentRequest request);

    /**
     * Orchestrates the process of updating an apartment's details.
     * Retrieves the current authenticated user context before delegating to the domain service.
     *
     * @param apartmentId the ID of the apartment
     * @param request     the update data
     */
    void updateApartment(Long apartmentId, UpdateApartmentRequest request);

    /**
     * Retrieves all apartments associated with the specified workspace ID.
     *
     * @param workspaceId the ID of the workspace
     * @return a list of CreateApartmentResponse representing the apartments
     */
    List<CreateApartmentResponse> getAllApartmentsInWorkspace(Long workspaceId);

    GetApartmentResponse getApartmentById(Long apartmentId);

}
