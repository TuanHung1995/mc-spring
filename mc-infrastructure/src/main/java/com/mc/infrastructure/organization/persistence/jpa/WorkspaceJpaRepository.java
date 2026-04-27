package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.WorkspaceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<UUID> findIdsByTeamId(UUID teamId);

    @Modifying
    @Query(value = "UPDATE org_workspaces SET is_deleted = true, updated_at = NOW(), deleted_at = NOW(), deleted_by = :deletedBy " +
            "WHERE team_id = :teamId " +
            "AND is_deleted = false", nativeQuery = true)
    void softDeleteByTeamId(@Param("teamId") UUID teamId, @Param("deletedBy") UUID deletedBy);

}
