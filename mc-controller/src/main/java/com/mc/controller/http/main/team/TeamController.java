package com.mc.controller.http.main.team;

import com.mc.application.model.team.*;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.application.service.team.TeamAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamAppService teamAppService;

    @PostMapping("/create-apartment")
    @PreAuthorize("hasPermission(#request.teamId, 'Team', 'APARTMENT:CREATE')")
    public ResponseEntity<CreateApartmentResponse> createApartment(@RequestBody CreateApartmentRequest request){
        return ResponseEntity.ok(teamAppService.createApartment(request));
    }

    @PostMapping("/add-member")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:ADD_MEMBER')")
    public ResponseEntity<List<UserProfileResponse>> addApartmentMember(@Valid @RequestBody AddApartmentMemberRequest request){
        return ResponseEntity.ok(teamAppService.addApartmentMember(request));
    }

    @DeleteMapping("/delete-team")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:DELETE')")
    public ResponseEntity<DeleteApartmentResponse> deleteApartment(@RequestBody DeleteApartmentRequest request){
        return ResponseEntity.ok(teamAppService.deleteApartmentById(request.getApartmentId()));
    }

    @DeleteMapping("/delete-member")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:DELETE_MEMBER')")
    public ResponseEntity<List<UserProfileResponse>> deleteApartmentMember(@RequestBody DeleteApartmentMemberRequest request) {
        return ResponseEntity.ok(teamAppService.deleteApartmentMember(request));
    }

    @PutMapping("/assign-owner")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:ASSIGN_OWNER')")
    public ResponseEntity<AssignApartmentOwnerResponse> assignApartmentOwner(@RequestBody AssignApartmentOwnerRequest request){
        return ResponseEntity.ok(teamAppService.assignApartmentOwner(request));
    }

    @PostMapping("/request-join-apartment")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:REQUEST_JOIN')")
    public ResponseEntity<RequestToJoinApartmentResponse> requestJoinApartment(@Valid @RequestBody RequestToJoinApartmentRequest request) {
        return ResponseEntity.ok(teamAppService.requestToJoinApartment(request));
    }

    @PostMapping("/accept-request/join-apartment")
//    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:APPROVE_REQUEST')")
    public ResponseEntity<?> acceptJoinApartmentRequest(@RequestBody ApproveRequestJoinApartmentRequest request) {
        teamAppService.approveRequestJoinApartment(request);
        return ResponseEntity.ok("Request processed successfully");
    }

}
