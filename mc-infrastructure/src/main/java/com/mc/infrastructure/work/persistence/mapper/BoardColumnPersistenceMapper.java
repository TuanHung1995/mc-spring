package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.BoardColumn;
import com.mc.domain.work.model.enums.BoardColumnType;
import com.mc.infrastructure.work.persistence.model.BoardColumnJpaEntity;
import org.springframework.stereotype.Component;

/**
 * BoardColumnPersistenceMapper — Infrastructure Mapper (Work Context)
 *
 * <p>Handles String ↔ {@link BoardColumnType} enum conversion when moving
 * between the pure domain and the JPA persistence layer.</p>
 */
@Component
public class BoardColumnPersistenceMapper {

    public BoardColumnJpaEntity toEntity(BoardColumn domain) {
        if (domain == null) return null;

        BoardColumnJpaEntity entity = new BoardColumnJpaEntity();
        entity.setId(domain.getId());
        entity.setBoardId(domain.getBoardId());
        entity.setTitle(domain.getTitle());
        entity.setType(domain.getType() != null ? domain.getType().name() : null);
        entity.setDescription(domain.getDescription());
        entity.setPosition(domain.getPosition());
        entity.setWidth(domain.getWidth());
        entity.setHidden(domain.isHidden());
        entity.setCreatedById(domain.getCreatedById());
        entity.setUpdatedById(domain.getUpdatedById());
        entity.setDeletedById(domain.getDeletedById());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public BoardColumn toDomain(BoardColumnJpaEntity entity) {
        if (entity == null) return null;

        BoardColumnType type = null;
        if (entity.getType() != null) {
            try { type = BoardColumnType.valueOf(entity.getType()); }
            catch (IllegalArgumentException ignored) { type = BoardColumnType.TEXT; }
        }

        return new BoardColumn(
                entity.getId(),
                entity.getBoardId(),
                entity.getTitle(),
                type,
                entity.getDescription(),
                entity.getPosition(),
                entity.getWidth(),
                entity.isHidden(),
                entity.getCreatedById(),
                entity.getUpdatedById(),
                entity.getDeletedById(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
