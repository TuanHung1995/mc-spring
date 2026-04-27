package com.mc.application.organization.service.impl;

import com.mc.application.organization.dto.response.TeamResponse;
import com.mc.application.organization.service.TeamAppService;
import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.organization.model.entity.Team;
import com.mc.domain.organization.model.entity.Workspace;
import com.mc.domain.organization.port.OrgUserContextPort;
import com.mc.domain.organization.port.OrgUserView;
import com.mc.domain.organization.port.out.WorkspaceMessagePort;
import com.mc.domain.organization.repository.TeamRepository;
import com.mc.domain.organization.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TeamAppServiceImpl — Application Service (Organization Context)
 *
 * <p>Orchestrates team creation. Delegates entity creation to the domain factory method
 * ({@link Team#createDefault(String, UUID)}) to ensure invariants are enforced.
 * This service does NOT contain any business logic itself — it coordinates between
 * the domain and the repository port.</p>
 *
 * <p><strong>PREVIOUS BUG FIXED:</strong>
 * The old implementation called {@code Team.createDefault(name, ownerId)} but used
 * undefined local variables {@code name} and {@code ownerId} instead of the method parameter.
 * This would fail at compile time. The signature mismatch ({@code String email} param
 * but trying to use {@code String name, UUID ownerId}) is now corrected.</p>
 */
@Service
@RequiredArgsConstructor
@Component("orgTeamAppService")
public class TeamAppServiceImpl implements TeamAppService {

    @Qualifier("orgTeamRepository")
    private final TeamRepository teamRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMessagePort workspaceMessagePort;
    private final OrgUserContextPort orgUserContextPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates a new team for the given user using the Team aggregate's factory method.
     *
     * <p>The factory method on {@link Team} ensures all invariants are met
     * (name is non-blank, ownerId is non-null) before the entity is ever persisted.</p>
     */
    @Override
    @Transactional
    public TeamResponse createTeam(String name, UUID ownerId) {
        // Delegate creation to the domain factory — business invariants live there, not here
        Team team = Team.createDefault(name, ownerId);
        Team saved = teamRepository.save(team);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TeamResponse> getTeamById(UUID id) {
        return teamRepository.findById(id).map(this::toResponse);
    }

    /** Maps domain entity to response DTO — keeps the domain model off the wire. */
    private TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getSlug(),
                team.getDescription()
        );
    }

    @Override
    @Transactional
    public void deleteTeam(UUID teamId) {

        OrgUserView currentUser = orgUserContextPort.getCurrentUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        team.softDelete(currentUser.id());
        teamRepository.save(team);

        List<UUID> workspaceIds = workspaceRepository.findIdsByTeamId(teamId);
        if (!workspaceIds.isEmpty()) {

            workspaceRepository.softDeleteByTeamId(team.getId(), currentUser.id());

            for (UUID wId : workspaceIds) {
                applicationEventPublisher.publishEvent(
                        new WorkspaceDeletedIntegrationEvent(wId, currentUser.id())
                );
            }
        }

    }
}
