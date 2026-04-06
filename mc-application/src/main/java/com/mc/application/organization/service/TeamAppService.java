package com.mc.application.organization.service;

import com.mc.application.organization.dto.response.TeamResponse;

import java.util.Optional;
import java.util.UUID;

/**
 * TeamAppService — Application Service Port (Organization Context)
 *
 * <p>Defines use-case operations for Team management.
 * Returns {@link TeamResponse} DTOs — never raw domain entities.</p>
 */
public interface TeamAppService {

    /**
     * Creates a new default team for the given user.
     *
     * @param name    The team's initial name.
     * @param ownerId The UUID of the user who will own the team.
     * @return The created team as a response DTO.
     */
    TeamResponse createTeam(String name, UUID ownerId);

    /**
     * Finds a team by its UUID.
     */
    Optional<TeamResponse> getTeamById(UUID id);
}
