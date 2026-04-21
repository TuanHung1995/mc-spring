package com.mc.application.organization.service.impl;

import com.mc.application.organization.dto.request.CreateWorkspaceRequest;
import com.mc.application.organization.dto.response.WorkspaceResponse;
import com.mc.application.organization.service.WorkspaceAppService;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.organization.model.entity.Workspace;
import com.mc.domain.organization.port.OrgUserContextPort;
import com.mc.domain.organization.port.OrgUserView;
import com.mc.domain.organization.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.organization.port.out.WorkspaceMessagePort;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * WorkspaceAppServiceImpl — Application Service (Organization Context)
 *
 * <p>Orchestrates use cases for Workspace management. The authenticated user's identity
 * is resolved from the security context via {@link OrgUserContextPort} — callers
 * never pass user IDs explicitly, eliminating the previous security vulnerability where
 * any client could spoof another user's identity.</p>
 *
 * <p>Business invariants (name validation, deletion guards) live in the {@link Workspace}
 * domain entity. This class is a pure orchestrator: resolve user → call domain → persist → map DTO.</p>
 */
@Service
@RequiredArgsConstructor
public class WorkspaceAppServiceImpl implements WorkspaceAppService {

    private final WorkspaceRepository workspaceRepository;
    private final OrgUserContextPort orgUserContextPort;
    private final WorkspaceMessagePort workspaceMessagePort;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {
        // Resolve creator from security context — NOT from request params
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        // Delegate creation to the domain factory (enforces invariants)
        Workspace workspace = Workspace.create(request.getName(), currentUser.id(), request.getTeamId());
        Workspace saved = workspaceRepository.save(workspace);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponse getWorkspaceById(UUID id) {
        return workspaceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceResponse> getAllWorkspacesByTeamId(UUID teamId) {
        return workspaceRepository.findAllByTeamId(teamId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteWorkspace(UUID id) {
        // Resolve deleter from security context for audit trail
        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace", "id", id));

        // Domain entity enforces deletion invariants (e.g., already-deleted guard)
        workspace.softDelete(currentUser.id());
        workspaceRepository.save(workspace);

        // Publish internal Integration Event to trigger Async cascades (e.g. Work Bounded Context)
        workspaceMessagePort.publishWorkspaceDeletedEvent(
                new WorkspaceDeletedIntegrationEvent(id, currentUser.id(), LocalDateTime.now())
        );
    }

    private WorkspaceResponse toResponse(Workspace workspace) {
        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getStatus() != null ? workspace.getStatus().name() : null,
                workspace.getTeamId()
        );
    }
}
