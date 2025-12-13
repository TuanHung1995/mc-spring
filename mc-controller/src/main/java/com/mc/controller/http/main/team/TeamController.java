package com.mc.controller.http.main.team;

import com.mc.application.model.team.AddApartmentMemberRequest;
import com.mc.application.model.team.CreateApartmentRequest;
import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.application.service.team.TeamAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("add-member")
    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'APARTMENT:ADD_MEMBER')")
    public ResponseEntity<List<UserProfileResponse>> addApartmentMember(@RequestBody AddApartmentMemberRequest request){
        return ResponseEntity.ok(teamAppService.addApartmentMember(request));
    }


}
