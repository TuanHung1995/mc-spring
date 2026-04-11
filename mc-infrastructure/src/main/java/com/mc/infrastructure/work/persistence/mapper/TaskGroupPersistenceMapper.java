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
        entity.setTitle(domain.getTitle());
        entity.setColor(domain.getColor());
        entity.setPosition(domain.getPosition());
        entity.setCollapsed(domain.isCollapsed());
        entity.setArchived(domain.isArchived());
        entity.setArchivedById(domain.getArchivedBy());
        entity.setCreatedById(domain.getCreatedBy());
        entity.setUpdatedById(domain.getUpdatedBy());
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
                entity.getTitle(),
                entity.getColor(),
                entity.getPosition(),
                entity.isCollapsed(),
                entity.isArchived(),
                entity.getArchivedAt(),
                entity.getArchivedById(),
                entity.getCreatedById(),
                entity.getUpdatedById(),
                entity.getDeletedById(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
