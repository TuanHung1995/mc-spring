package com.mc.controller.organization.http;

import com.mc.application.organization.dto.request.*;
import com.mc.application.organization.dto.response.ApartmentMemberResponse;
import com.mc.application.organization.dto.response.ApartmentResponse;
import com.mc.application.organization.service.ApartmentAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * ApartmentController — HTTP Adapter (Organization Context)
 *
 * <p>Exposes Apartment CRUD and the full ApartmentMember lifecycle as REST endpoints.
 * All acting-user resolution is done internally via the JWT security context —
 * no userId parameters are accepted from HTTP clients.</p>
 *
 * <p><strong>ENDPOINT DESIGN:</strong>
 * Member management endpoints are nested under {@code /api/v1/apartments/members/}
 * to keep them semantically grouped under their parent resource.</p>
 */
@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentAppService apartmentAppService;

    // =================================================================
    // APARTMENT CRUD
    // =================================================================

    @PostMapping
    public ResponseEntity<ApartmentResponse> createApartment(
            @Valid @RequestBody CreateApartmentRequest request) {
        return ResponseEntity.ok(apartmentAppService.createApartment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponse> getApartment(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentAppService.getApartmentById(id));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<ApartmentResponse>> getApartmentsByWorkspace(
            @PathVariable UUID workspaceId) {
        return ResponseEntity.ok(apartmentAppService.getAllApartmentsByWorkspaceId(workspaceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponse> updateApartment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApartmentRequest request) {
        return ResponseEntity.ok(apartmentAppService.updateApartment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable UUID id) {
        apartmentAppService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // MEMBER MANAGEMENT
    // =================================================================

    /**
     * POST /api/v1/apartments/members/add
     *
     * <p>Owner invites a user by userId UUID into the apartment.
     * Requires: caller is apartment owner.</p>
     */
    @PostMapping("/members/add")
    public ResponseEntity<ApartmentMemberResponse> addMember(
            @Valid @RequestBody AddMemberRequest request) {
        return ResponseEntity.ok(apartmentAppService.addMember(request));
    }

    /**
     * POST /api/v1/apartments/members/request-join
     *
     * <p>Any authenticated user self-requests to join an apartment.
     * Creates a PENDING membership that the owner must approve.</p>
     */
    @PostMapping("/members/request-join")
    public ResponseEntity<ApartmentMemberResponse> requestToJoin(
            @Valid @RequestBody RequestToJoinRequest request) {
        return ResponseEntity.ok(apartmentAppService.requestToJoin(request));
    }

    /**
     * POST /api/v1/apartments/members/approve
     *
     * <p>Owner approves or rejects a PENDING membership.
     * {@code approve=true} → ACTIVE. {@code approve=false} → REJECTED.</p>
     */
    @PostMapping("/members/approve")
    public ResponseEntity<ApartmentMemberResponse> approveOrRejectRequest(
            @Valid @RequestBody ApproveRequestRequest request) {
        return ResponseEntity.ok(apartmentAppService.approveOrRejectRequest(request));
    }

    /**
     * POST /api/v1/apartments/members/assign-owner
     *
     * <p>Current owner transfers ownership to another ACTIVE member.
     * The caller loses owner status; the target gains it atomically.</p>
     */
    @PostMapping("/members/assign-owner")
    public ResponseEntity<ApartmentMemberResponse> assignOwner(
            @Valid @RequestBody AssignOwnerRequest request) {
        return ResponseEntity.ok(apartmentAppService.assignOwner(request));
    }

    /**
     * DELETE /api/v1/apartments/members/remove
     *
     * <p>Owner removes a member from the apartment.
     * The owner cannot remove themselves.</p>
     */
    @DeleteMapping("/members/remove")
    public ResponseEntity<Void> removeMember(
            @Valid @RequestBody RemoveMemberRequest request) {
        apartmentAppService.removeMember(request);
        return ResponseEntity.noContent().build();
    }
}
