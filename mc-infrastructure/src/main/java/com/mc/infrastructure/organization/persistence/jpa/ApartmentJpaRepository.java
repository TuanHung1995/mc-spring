package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.ApartmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ApartmentJpaRepository — Spring Data JPA Repository
 *
 * <p>Uses UUID as the PK type, matching the {@code org_apartments.id BINARY(16)} column.</p>
 */
@Repository
public interface ApartmentJpaRepository extends JpaRepository<ApartmentJpaEntity, UUID> {

    /**
     * Finds all apartments belonging to a workspace.
     *
     * @param workspaceId UUID of the parent Workspace.
     */
    List<ApartmentJpaEntity> findAllByWorkspaceId(UUID workspaceId);
}
