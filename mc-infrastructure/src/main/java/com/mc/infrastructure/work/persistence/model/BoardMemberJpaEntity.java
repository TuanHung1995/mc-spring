package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.persistence.model.BaseLongJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "work_board_members")
@Getter
@Setter
public class BoardMemberJpaEntity extends BaseLongJpaEntity {

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    private UUID deletedBy;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
