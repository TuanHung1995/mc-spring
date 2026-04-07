package com.mc.application.organization.service.impl;

import com.mc.application.organization.dto.request.*;
import com.mc.application.organization.dto.response.ApartmentMemberResponse;
import com.mc.application.organization.dto.response.ApartmentResponse;
import com.mc.application.organization.service.ApartmentAppService;
import com.mc.domain.exception.DomainException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.organization.model.entity.Apartment;
import com.mc.domain.organization.model.entity.ApartmentMember;
import com.mc.domain.organization.port.OrgUserContextPort;
import com.mc.domain.organization.port.OrgUserView;
import com.mc.domain.organization.repository.ApartmentMemberRepository;
import com.mc.domain.organization.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ApartmentAppServiceImpl — Application Service (Organization Context)
 *
 * <p>Implements the full Apartment and ApartmentMember use cases.
 * This class is a pure orchestrator — it coordinates between domain entities,
 * repository ports, and external ports. Business invariants always live in the domain.</p>
 *
 * <p><strong>AUTHORIZATION PATTERN:</strong>
 * Every write operation that requires owner privilege follows this pattern:
 * <ol>
 *   <li>Resolve acting user from JWT security context via {@link OrgUserContextPort}.</li>
 *   <li>Load the caller's membership record from the repository.</li>
 *   <li>Assert {@code member.isOwner()} — if false, throw {@link DomainException}.</li>
 *   <li>Proceed with the use case logic.</li>
 * </ol>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ApartmentAppServiceImpl implements ApartmentAppService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMemberRepository memberRepository;
    private final OrgUserContextPort orgUserContextPort;
    private final Long ownerApartmentId = 6L;

    // =================================================================
    // APARTMENT CRUD
    // =================================================================

    @Override
    @Transactional
    public ApartmentResponse createApartment(CreateApartmentRequest request) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();
        Apartment apartment = Apartment.create(
                request.getName(), request.getDescription(), request.getBackgroundUrl(),
                currentUser.id(), request.getTeamId(), request.getWorkspaceId()
        );
        Apartment saved = apartmentRepository.save(apartment);
        addCreatorToApartment(saved, currentUser.id());
        return toApartmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ApartmentResponse getApartmentById(UUID id) {
        return apartmentRepository.findById(id)
                .map(this::toApartmentResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApartmentResponse> getAllApartmentsByWorkspaceId(UUID workspaceId) {
        return apartmentRepository.findAllByWorkspaceId(workspaceId).stream()
                .map(this::toApartmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApartmentResponse updateApartment(UUID id, UpdateApartmentRequest request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));
        apartment.updateDetails(request.getName(), request.getDescription(), request.getBackgroundUrl());
        return toApartmentResponse(apartmentRepository.save(apartment));
    }

    @Override
    @Transactional
    public void deleteApartment(UUID id) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", "id", id));
        apartment.softDelete(currentUser.id());
        apartmentRepository.save(apartment);
    }

    private void addCreatorToApartment(Apartment apartment, UUID userId) {
        ApartmentMember creator = ApartmentMember.createOwner(apartment.getId(), userId, ownerApartmentId);
        memberRepository.save(creator);
    }

    // =================================================================
    // MEMBER MANAGEMENT
    // =================================================================

    /**
     * ADD MEMBER — Apartment owner directly invites a user.
     *
     * <p><strong>BUSINESS RULES:</strong>
     * <ul>
     *   <li>Caller must be the apartment owner.</li>
     *   <li>Target user must not already have a membership record (any status)
     *       to prevent duplicate invitations.</li>
     * </ul>
     * </p>
     */
    @Override
    @Transactional
    public ApartmentMemberResponse addMember(AddMemberRequest request) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();
        requireOwner(request.getApartmentId(), currentUser.id());

        // Guard: prevent duplicate invitations
        if (memberRepository.existsByApartmentIdAndUserId(request.getApartmentId(), request.getUserId())) {
            throw new DomainException("User already has a membership record in this apartment");
        }

        ApartmentMember invitation = ApartmentMember.createInvitation(
                request.getApartmentId(), request.getUserId(), request.getRoleId());
        return toMemberResponse(memberRepository.save(invitation));
    }

    /**
     * REQUEST TO JOIN — Authenticated user self-requests membership.
     *
     * <p><strong>BUSINESS RULES:</strong>
     * <ul>
     *   <li>User must not already have a membership record in this apartment.</li>
     * </ul>
     * </p>
     */
    @Override
    @Transactional
    public ApartmentMemberResponse requestToJoin(RequestToJoinRequest request) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        // Guard: prevent duplicate join requests
        if (memberRepository.existsByApartmentIdAndUserId(request.getApartmentId(), currentUser.id())) {
            throw new DomainException("You already have a membership record in this apartment");
        }

        // roleId = null → means "no specific role yet" (will be assigned on approval by owner)
        ApartmentMember joinRequest = ApartmentMember.createJoinRequest(
                request.getApartmentId(), currentUser.id(), null);
        return toMemberResponse(memberRepository.save(joinRequest));
    }

    /**
     * APPROVE OR REJECT — Apartment owner approves or rejects a PENDING membership.
     *
     * <p><strong>BUSINESS RULES:</strong>
     * <ul>
     *   <li>Caller must be the apartment owner.</li>
     *   <li>Target membership must be PENDING — the domain entity enforces this.</li>
     * </ul>
     * </p>
     */
    @Override
    @Transactional
    public ApartmentMemberResponse approveOrRejectRequest(ApproveRequestRequest request) {
        ApartmentMember membership = memberRepository.findById(request.getMembershipId())
                .orElseThrow(() -> new ResourceNotFoundException("ApartmentMember", "id", request.getMembershipId()));

        OrgUserView currentUser = orgUserContextPort.getCurrentUser();
        requireOwner(membership.getApartmentId(), currentUser.id());

        if (request.isApprove()) {
            // Domain entity enforces: only PENDING memberships can be accepted
            membership.acceptInvitation();
        } else {
            // Domain entity enforces: only non-REJECTED memberships can be rejected
            membership.reject();
        }

        return toMemberResponse(memberRepository.save(membership));
    }

    /**
     * ASSIGN OWNER — Current owner transfers ownership to an ACTIVE member.
     *
     * <p><strong>BUSINESS RULES:</strong>
     * <ul>
     *   <li>Caller must be the current owner.</li>
     *   <li>New owner must be an ACTIVE member — the domain entity enforces this in promoteToOwner().</li>
     *   <li>Ownership transfer is atomic: demote current owner FIRST, then promote new owner,
     *       all within a single transaction so we never have 0 or 2 owners at commit time.</li>
     * </ul>
     * </p>
     */
    @Override
    @Transactional
    public ApartmentMemberResponse assignOwner(AssignOwnerRequest request) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        System.out.println(currentUser.id());
        // Load current owner's membership and assert
        ApartmentMember currentOwnerMembership = requireOwner(request.getApartmentId(), currentUser.id());


        // Load the new owner's membership — must be an ACTIVE member
        ApartmentMember newOwnerMembership = memberRepository
                .findByApartmentIdAndUserId(request.getApartmentId(), request.getNewOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ApartmentMember", "userId", request.getNewOwnerId()));

        // Guard: cannot assign owner to yourself (already the owner)
        if (currentUser.id().equals(request.getNewOwnerId())) {
            throw new DomainException("You are already the owner of this apartment");
        }

        // Atomic ownership transfer:
        // Step 1: demote current owner (clears owner flag)
        currentOwnerMembership.demoteFromOwner();
        memberRepository.save(currentOwnerMembership);

        // Step 2: promote new owner (domain enforces: must be ACTIVE)
        newOwnerMembership.promoteToOwner();
        ApartmentMember savedNewOwner = memberRepository.save(newOwnerMembership);

        // Also update the Apartment entity's ownerId for consistency
        apartmentRepository.findById(request.getApartmentId()).ifPresent(apartment -> {
            apartment.transferOwnership(request.getNewOwnerId());
            apartmentRepository.save(apartment);
        });

        return toMemberResponse(savedNewOwner);
    }

    /**
     * REMOVE MEMBER — Apartment owner removes a member.
     *
     * <p><strong>BUSINESS RULES:</strong>
     * <ul>
     *   <li>Caller must be the apartment owner.</li>
     *   <li>Caller cannot remove themselves (the owner cannot remove their own membership).</li>
     * </ul>
     * </p>
     */
    @Override
    @Transactional
    public void removeMember(RemoveMemberRequest request) {
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();
        requireOwner(request.getApartmentId(), currentUser.id());

        // Guard: owner cannot remove themselves via this endpoint
        if (currentUser.id().equals(request.getUserId())) {
            throw new DomainException(
                    "An owner cannot remove themselves. Transfer ownership first, then leave.");
        }

        ApartmentMember membership = memberRepository
                .findByApartmentIdAndUserId(request.getApartmentId(), request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ApartmentMember", "userId", request.getUserId()));

        // Soft-delete the membership record
        memberRepository.delete(membership.getId());
    }

    // =================================================================
    // PRIVATE HELPERS
    // =================================================================

    /**
     * Asserts the given user is the owner of the apartment.
     *
     * @return The caller's ApartmentMember record for further use.
     * @throws ResourceNotFoundException if the caller has no membership.
     * @throws DomainException           if the caller is not the owner.
     */
    private ApartmentMember requireOwner(UUID apartmentId, UUID userId) {
        ApartmentMember membership = memberRepository
                .findByApartmentIdAndUserId(apartmentId, userId)
                .orElseThrow(() -> new DomainException("You are not a member of this apartment"));

        System.out.println(membership.getRoleId());
        System.out.println(membership.isOwner());
        if (!membership.isOwner()) {

            throw new DomainException("Only the apartment owner can perform this action");
        }
        return membership;
    }

    private ApartmentResponse toApartmentResponse(Apartment apartment) {
        return new ApartmentResponse(
                apartment.getId(), apartment.getName(), apartment.getDescription(),
                apartment.getBackgroundUrl(), apartment.getOwnerId(),
                apartment.getTeamId(), apartment.getWorkspaceId()
        );
    }

    private ApartmentMemberResponse toMemberResponse(ApartmentMember member) {
        return new ApartmentMemberResponse(
                member.getId(), member.getApartmentId(), member.getUserId(),
                member.getRoleId(), member.isOwner(),
                member.getStatus() != null ? member.getStatus().name() : null,
                member.getJoinedAt()
        );
    }
}
