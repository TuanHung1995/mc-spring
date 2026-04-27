package com.mc.domain.work.model.entity;

import com.mc.domain.work.model.BaseWorkEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class BoardMember extends BaseWorkEntity {

    private UUID boardId;
    private UUID userId;
    private Long roleId;
    private UUID createdBy;
    private UUID deletedBy;
    private UUID updatedBy;
    private LocalDateTime joinedAt;
    private LocalDateTime deletedAt;

    private BoardMember() {}

    public BoardMember(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
                       boolean deleted, UUID boardId, UUID userId, Long roleId,
                       UUID createdBy, UUID deletedBy, UUID updatedBy, LocalDateTime joinedAt,
                       LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deleted);
        this.boardId = boardId;
        this.userId = userId;
        this.roleId = roleId;
        this.createdBy = createdBy;
        this.deletedBy = deletedBy;
        this.updatedBy = updatedBy;
        this.joinedAt = joinedAt;
        this.deletedAt = deletedAt;
    }

    public static BoardMember addMember(UUID boardId, UUID userId, Long roleId) {
        BoardMember member = new BoardMember();
        member.initNew(null);
        member.boardId = boardId;
        member.userId = userId;
        member.roleId = roleId;
        member.createdBy = userId;
        member.joinedAt = LocalDateTime.now();
        return member;
    }

    public void updateRole(Long newRoleId) {
        this.roleId = newRoleId;
        touch();
    }
}
