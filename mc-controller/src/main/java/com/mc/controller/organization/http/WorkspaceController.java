package com.mc.controller.organization.http;

import com.mc.application.organization.dto.request.CreateWorkspaceRequest;
import com.mc.application.organization.dto.response.WorkspaceResponse;
import com.mc.application.organization.service.WorkspaceAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * WorkspaceController — HTTP Adapter (Organization Context)
 *
 * <p><strong>SECURITY FIX:</strong>
 * The previous version accepted {@code @RequestParam Long currentUserId}, which allowed
 * any authenticated user to create or delete workspaces on behalf of any other user.
 * The {@code currentUserId} is now resolved internally by the application service
 * via {@link com.mc.domain.organization.port.OrgUserContextPort}, reading from the
 * JWT-backed Spring Security context. The HTTP client never controls which user is acting.</p>
 */
@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceAppService workspaceAppService;

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request) {
        // Creator is resolved from JWT security context — not from request params
        return ResponseEntity.ok(workspaceAppService.createWorkspace(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(@PathVariable UUID id) {
        return ResponseEntity.ok(workspaceAppService.getWorkspaceById(id));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<WorkspaceResponse>> getWorkspacesByTeam(@PathVariable UUID teamId) {
        return ResponseEntity.ok(workspaceAppService.getAllWorkspacesByTeamId(teamId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceAppService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
}
