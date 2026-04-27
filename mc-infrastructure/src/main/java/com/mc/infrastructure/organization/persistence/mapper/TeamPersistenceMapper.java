package com.mc.infrastructure.organization.persistence.mapper;

import com.mc.domain.organization.model.entity.Team;
import com.mc.infrastructure.organization.persistence.model.TeamJpaEntity;
import org.springframework.stereotype.Component;

@Component("orgTeamPersistenceMapper")
public class TeamPersistenceMapper {

    public TeamJpaEntity toEntity(Team domain) {
        if (domain == null) return null;

        TeamJpaEntity entity = new TeamJpaEntity();
        // Only set ID if entity is being updated (ID exists), not for new inserts
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setSlug(domain.getSlug());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedBy(domain.getDeletedBy());
        return entity;
    }

    public Team toDomain(TeamJpaEntity entity) {
        if (entity == null) return null;

        return new Team(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSlug(),
                entity.getCreatedBy(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
