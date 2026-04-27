package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * BoardJpaEntity — Infrastructure JPA Entity (Work Context)
 *
 * <p>Maps to the {@code boards} table. Uses {@code BIGINT AUTO_INCREMENT} PK (legacy schema).</p>
 * <p>Uses Hibernate {@code @SoftDelete} to handle the {@code is_deleted} flag natively.</p>
 */
@Entity
@Table(name = "work_boards")
@Getter
@Setter
public class BoardJpaEntity extends BaseJpaEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "workspace_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID workspaceId;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedById;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
