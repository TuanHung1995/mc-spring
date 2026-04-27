package com.mc.domain.organization.repository;

import com.mc.domain.organization.model.entity.Workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * WorkspaceRepository — Domain Repository Port (Organization Context)
 *
 * <p>Outbound port defining persistence operations for Workspaces.
 * All IDs are UUID to match the {@code org_workspaces} table schema (BINARY(16) PK).</p>
 *
 * <p>The implementation lives in mc-infrastructure as {@code WorkspaceRepositoryImpl}.</p>
 */
public interface WorkspaceRepository {

    /** Persists a new or updated Workspace. Returns the saved state (with auto-set timestamps). */
    Workspace save(Workspace workspace);

    /** Finds a Workspace by its UUID primary key. */
    Optional<Workspace> findById(UUID id);

    /**
     * Finds all non-deleted Workspaces belonging to a Team.
     *
     * @param teamId UUID of the parent Team.
     */
    List<Workspace> findAllByTeamId(UUID teamId);

    /**
     * Soft-deletes a workspace.
     * Prefer calling {@link Workspace#softDelete(UUID)} and then {@link #save(Workspace)}
     * so that the domain invariants are enforced before persistence.
     */
    void delete(UUID id);

    List<UUID> findIdsByTeamId(UUID teamId);

    void softDeleteByTeamId(UUID teamId, UUID deletedBy);
}
