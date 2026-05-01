package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.TaskGroup;
import com.mc.infrastructure.work.persistence.model.TaskGroupJpaEntity;
import org.springframework.stereotype.Component;

/**
 * TaskGroupPersistenceMapper — Infrastructure Mapper (Work Context)
 */
@Component
public class TaskGroupPersistenceMapper {

    public TaskGroupJpaEntity toEntity(TaskGroup domain) {
        if (domain == null) return null;

        TaskGroupJpaEntity entity = new TaskGroupJpaEntity();
        entity.setId(domain.getId());
        entity.setBoardId(domain.getBoardId());
        entity.setWorkspaceId(domain.getWorkspaceId());
        entity.setTeamId(domain.getTeamId());
        entity.setTitle(domain.getTitle());
        entity.setColor(domain.getColor());
        entity.setPosition(domain.getPosition());
        entity.setCollapsed(domain.isCollapsed());
        entity.setArchived(domain.isArchived());
        entity.setDeleted(domain.isDeleted());
        entity.setArchivedById(domain.getArchivedBy());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedById(domain.getDeletedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setArchivedAt(domain.getArchivedAt());
        return entity;
    }

    public TaskGroup toDomain(TaskGroupJpaEntity entity) {
        if (entity == null) return null;

        return new TaskGroup(
                entity.getId(),
                entity.getBoardId(),
                entity.getWorkspaceId(),
                entity.getTeamId(),
                entity.getTitle(),
                entity.getColor(),
                entity.getPosition(),
                entity.isCollapsed(),
                entity.isArchived(),
                entity.getArchivedAt(),
                entity.getArchivedById(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getDeletedById(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
