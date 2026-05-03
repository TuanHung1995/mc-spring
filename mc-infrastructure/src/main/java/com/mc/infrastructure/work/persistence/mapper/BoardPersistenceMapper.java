package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.Board;
import com.mc.domain.work.model.enums.BoardType;
import com.mc.infrastructure.work.persistence.model.BoardJpaEntity;
import org.springframework.stereotype.Component;

/**
 * BoardPersistenceMapper — Infrastructure Mapper (Work Context)
 *
 * <p>Translates between the pure domain {@link Board} and {@link BoardJpaEntity}.
 * Handles {@code BoardType} enum ↔ String conversion for DB storage.</p>
 */
@Component
public class BoardPersistenceMapper {

    public BoardJpaEntity toEntity(Board domain) {
        if (domain == null) return null;

        BoardJpaEntity entity = new BoardJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setType(domain.getType() != null ? domain.getType().name() : null);
        entity.setPurpose(domain.getPurpose());
        entity.setWorkspaceId(domain.getWorkspaceId());
        entity.setTeamId(domain.getTeamId());
        entity.setCreatedBy(domain.getCreatedById());
        entity.setDeletedById(domain.getDeletedById());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Board toDomain(BoardJpaEntity entity) {
        if (entity == null) return null;

        BoardType type = null;
        if (entity.getType() != null) {
            try { type = BoardType.valueOf(entity.getType()); }
            catch (IllegalArgumentException ignored) { type = BoardType.PUBLIC; }
        }

        return new Board(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                type,
                entity.getPurpose(),
                entity.getWorkspaceId(),
                entity.getTeamId(),
                entity.getCreatedBy(),
                entity.getDeletedById(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
