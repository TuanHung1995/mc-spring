package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.WorkspaceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * WorkspaceJpaRepository — Spring Data JPA Repository
 *
 * <p>Now uses UUID as the PK type, matching the {@code org_workspaces.id BINARY(16)} column.</p>
 */
@Repository
public interface WorkspaceJpaRepository extends JpaRepository<WorkspaceJpaEntity, UUID> {

    /**
     * Finds all workspaces belonging to a team.
     *
     * @param teamId UUID of the parent Team.
     */
    List<WorkspaceJpaEntity> findAllByTeamId(UUID teamId);
}
