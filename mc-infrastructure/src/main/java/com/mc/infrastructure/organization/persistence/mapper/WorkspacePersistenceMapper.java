package com.mc.infrastructure.organization.persistence.mapper;

import com.mc.domain.organization.model.entity.Workspace;
import com.mc.domain.organization.model.enums.WorkspaceStatus;
import com.mc.infrastructure.organization.persistence.model.WorkspaceJpaEntity;
import org.springframework.stereotype.Component;

/**
 * WorkspacePersistenceMapper — Infrastructure Mapper (Organization Context)
 *
 * <p>Translates between the pure domain {@link Workspace} and the JPA entity
 * {@link WorkspaceJpaEntity}. This class is the ONLY place where the domain
 * model and the persistence model touch each other.</p>
 *
 * <p><strong>RECONSTITUTION PATTERN:</strong>
 * We use the full-arg constructor on the domain side to reconstitute existing
 * entities from persistence, bypassing factory method invariants (which are
 * only for NEW entity creation). Data previously validated and persisted can
 * be trusted for reconstitution.</p>
 *
 * <p><strong>STATUS MAPPING:</strong>
 * The DB stores status as VARCHAR. We map it to the {@link WorkspaceStatus} enum
 * here in the mapper — the domain never sees raw strings.</p>
 */
@Component
public class WorkspacePersistenceMapper {

    /**
     * Converts a domain {@link Workspace} to its JPA entity representation for persistence.
     *
     * <p>Only set the ID if it exists (for updates). For new entities, let the
     * @GeneratedValue strategy handle ID generation on first flush.</p>
     */
    public WorkspaceJpaEntity toEntity(Workspace domain) {
        if (domain == null) return null;

        WorkspaceJpaEntity entity = new WorkspaceJpaEntity();
        // Only set ID if entity is being updated (ID exists), not for new inserts
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setName(domain.getName());
        // Convert enum → string for DB storage
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        entity.setTeamId(domain.getTeamId());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setDeletedBy(domain.getDeletedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        // Note: createdAt / updatedAt / createdBy / updatedBy are managed by
        // Spring Data Auditing (@CreatedDate, @LastModifiedDate, etc.) in BaseJpaEntity.
        // We do NOT set them manually here to avoid overwriting the auditing mechanism.
        entity.setDeleted(domain.isDeleted());
        return entity;
    }

    /**
     * Reconstitutes a {@link Workspace} domain entity from its JPA entity.
     */
    public Workspace toDomain(WorkspaceJpaEntity entity) {
        if (entity == null) return null;

        // Parse string → enum; default to ACTIVE if unrecognized (defensive)
        WorkspaceStatus status;
        try {
            status = entity.getStatus() != null
                    ? WorkspaceStatus.valueOf(entity.getStatus())
                    : WorkspaceStatus.ACTIVE;
        } catch (IllegalArgumentException e) {
            status = WorkspaceStatus.ACTIVE;
        }

        return new Workspace(
                entity.getId(),
                entity.getTeamId(),
                entity.getName(),
                status,
                entity.getCreatedBy(),
                entity.getDeletedBy(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                Boolean.TRUE.equals(entity.getDeleted())
        );
    }
}
