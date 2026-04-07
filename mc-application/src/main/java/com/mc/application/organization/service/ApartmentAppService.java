package com.mc.application.organization.service;

import com.mc.application.organization.dto.request.*;
import com.mc.application.organization.dto.response.ApartmentMemberResponse;
import com.mc.application.organization.dto.response.ApartmentResponse;

import java.util.List;
import java.util.UUID;

/**
 * ApartmentAppService — Application Service Port (Organization Context)
 *
 * <p>Covers both Apartment CRUD and the full ApartmentMember lifecycle:
 * Add, Request-to-join, Approve/Reject, Assign-owner, Remove.</p>
 *
 * <p>All methods resolve the acting user from the JWT security context via
 * {@code OrgUserContextPort} — no userId is ever accepted as a request parameter.</p>
 */
public interface ApartmentAppService {

    // =================================================================
    // APARTMENT CRUD
    // =================================================================

    ApartmentResponse createApartment(CreateApartmentRequest request);
    ApartmentResponse getApartmentById(UUID id);
    List<ApartmentResponse> getAllApartmentsByWorkspaceId(UUID workspaceId);
    ApartmentResponse updateApartment(UUID id, UpdateApartmentRequest request);
    void deleteApartment(UUID id);

    // =================================================================
    // MEMBER MANAGEMENT
    // =================================================================

    /**
     * An apartment owner directly invites a known user (by userId UUID) into the apartment.
     * Creates a PENDING membership. Only callable by the apartment owner.
     */
    ApartmentMemberResponse addMember(AddMemberRequest request);

    /**
     * An authenticated user self-requests to join an apartment.
     * Creates a PENDING membership. The apartment owner must then approve/reject.
     */
    ApartmentMemberResponse requestToJoin(RequestToJoinRequest request);

    /**
     * An apartment owner approves or rejects a PENDING membership.
     * Approve → ACTIVE. Reject → REJECTED.
     */
    ApartmentMemberResponse approveOrRejectRequest(ApproveRequestRequest request);

    /**
     * The current owner transfers ownership to another ACTIVE member.
     * The caller loses owner status; the newOwnerId gains it.
     * Both changes happen atomically in a single transaction.
     */
    ApartmentMemberResponse assignOwner(AssignOwnerRequest request);

    /**
     * An apartment owner removes a member from the apartment.
     * The owner cannot remove themselves.
     * The member's record is soft-deleted.
     */
    void removeMember(RemoveMemberRequest request);
}
