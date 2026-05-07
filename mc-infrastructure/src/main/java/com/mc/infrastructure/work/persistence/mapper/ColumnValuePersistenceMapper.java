package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.ColumnValue;
import com.mc.infrastructure.work.persistence.model.ColumnValueJpaEntity;
import org.springframework.stereotype.Component;

/**
 * ColumnValuePersistenceMapper — Infrastructure Mapper (Work Context)
 */
@Component
public class ColumnValuePersistenceMapper {

    public ColumnValueJpaEntity toEntity(ColumnValue domain) {
        if (domain == null) return null;

        ColumnValueJpaEntity entity = new ColumnValueJpaEntity();
        entity.setId(domain.getId());
        entity.setItemId(domain.getItemId());
        entity.setColumnId(domain.getColumnId());
        entity.setBoardId(domain.getBoardId());
        entity.setTaskGroupId(domain.getTaskGroupId());
        entity.setWorkspaceId(domain.getWorkspaceId());
        entity.setTeamId(domain.getTeamId());
        entity.setValue(domain.getValue());
        entity.setTextValue(domain.getTextValue());
        entity.setColor(domain.getColor());
        entity.setType(domain.getType());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public ColumnValue toDomain(ColumnValueJpaEntity entity) {
        if (entity == null) return null;

        return new ColumnValue(
                entity.getId(),
                entity.getItemId(),
                entity.getColumnId(),
                entity.getBoardId(),
                entity.getTaskGroupId(),
                entity.getWorkspaceId(),
                entity.getTeamId(),
                entity.getValue(),
                entity.getTextValue(),
                entity.getColor(),
                entity.getType(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
