package com.mc.application.organization.service;

import com.mc.application.organization.dto.request.CreateWorkspaceRequest;
import com.mc.application.organization.dto.response.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

/**
 * WorkspaceAppService — Application Service Port (Organization Context)
 *
 * <p>All IDs are UUID. The authenticated user is resolved internally via
 * {@code OrgUserContextPort} — callers never pass {@code currentUserId} explicitly.</p>
 */
public interface WorkspaceAppService {

    /**
     * Creates a new workspace. The creator's ID is resolved from the security context.
     */
    WorkspaceResponse createWorkspace(CreateWorkspaceRequest request);

    WorkspaceResponse getWorkspaceById(UUID id);

    List<WorkspaceResponse> getAllWorkspacesByTeamId(UUID teamId);

    void deleteWorkspace(UUID id);
}
