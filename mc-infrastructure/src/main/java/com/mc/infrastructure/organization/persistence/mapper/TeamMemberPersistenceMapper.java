package com.mc.infrastructure.organization.persistence.mapper;

import com.mc.domain.organization.model.entity.TeamMember;
import com.mc.infrastructure.organization.persistence.model.TeamMemberJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberPersistenceMapper {

    public TeamMemberJpaEntity toEntity(TeamMember domain) {
        if (domain == null) return null;

        TeamMemberJpaEntity entity = new TeamMemberJpaEntity();
        // Only set ID if entity is being updated (ID exists), not for new inserts
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }

        entity.setUserId(domain.getUserId());
        entity.setTeamId(domain.getTeamId());
        entity.setRoleId(domain.getRoleId());
        entity.setJoinedAt(domain.getJoinedAt());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public TeamMember toDomain(TeamMemberJpaEntity entity) {
        if (entity == null) return null;

        return new TeamMember(
                entity.getId(),
                entity.getUserId(),
                entity.getTeamId(),
                entity.getRoleId(),
                entity.getJoinedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.isDeleted()
        );
    }

}
