package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.Item;
import com.mc.infrastructure.work.persistence.model.ItemJpaEntity;
import org.springframework.stereotype.Component;

/**
 * ItemPersistenceMapper — Infrastructure Mapper (Work Context)
 */
@Component
public class ItemPersistenceMapper {

    public ItemJpaEntity toEntity(Item domain) {
        if (domain == null) return null;

        ItemJpaEntity entity = new ItemJpaEntity();
        entity.setId(domain.getId());
        entity.setBoardId(domain.getBoardId());
        entity.setGroupId(domain.getGroupId());
        entity.setWorkspaceId(domain.getWorkspaceId());
        entity.setTeamId(domain.getTeamId());
        entity.setName(domain.getName());
        entity.setPosition(domain.getPosition());
        entity.setDeleted(domain.isDeleted());
        entity.setCreatedBy(domain.getCreatedById());
        entity.setUpdatedBy(domain.getUpdatedById());
        entity.setDeletedById(domain.getDeletedById());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Item toDomain(ItemJpaEntity entity) {
        if (entity == null) return null;

        return new Item(
                entity.getId(),
                entity.getBoardId(),
                entity.getGroupId(),
                entity.getWorkspaceId(),
                entity.getTeamId(),
                entity.getName(),
                entity.getPosition(),
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
