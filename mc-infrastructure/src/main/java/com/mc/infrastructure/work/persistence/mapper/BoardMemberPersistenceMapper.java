package com.mc.infrastructure.work.persistence.mapper;

import com.mc.domain.work.model.entity.BoardMember;
import com.mc.infrastructure.work.persistence.model.BoardMemberJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardMemberPersistenceMapper {

    public BoardMemberJpaEntity toEntity(BoardMember domain) {
        if (domain == null) return null;

        BoardMemberJpaEntity entity = new BoardMemberJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setBoardId(domain.getBoardId());
        entity.setRoleId(domain.getRoleId());
        entity.setCreatedById(domain.getCreatedBy());
        entity.setUpdatedById(domain.getUpdatedBy());
        entity.setDeletedBy(domain.getDeletedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
//        entity.setDeleted(domain.getDeleted());
        entity.setJoinedAt(domain.getJoinedAt());
        return entity;
    }

    public BoardMember toDomain(BoardMemberJpaEntity entity) {
        if (entity == null) return null;

        return new BoardMember(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted(),
                entity.getBoardId(),
                entity.getUserId(),
                entity.getRoleId(),
                entity.getCreatedById(),
                entity.getDeletedBy(),
                entity.getUpdatedById(),
                entity.getJoinedAt(),
                entity.getDeletedAt()
        );
    }

}
