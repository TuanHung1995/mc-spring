package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.persistence.model.BaseLongJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * TaskGroupJpaEntity — Infrastructure JPA Entity (Work Context)
 *
 * <p>Maps to the {@code task_groups} table. BIGINT AUTO_INCREMENT PK.
 * Uses Hibernate @SoftDelete + @Filter for soft-delete + archived filtering.</p>
 */
@Entity
@Table(name = "work_task_groups")
@Getter
@Setter
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
@FilterDef(name = "deleted_wtg_Filter", parameters = @ParamDef(name = "deleted", type = Boolean.class))
@Filter(name = "deleted_wtg_Filter", condition = "is_deleted = :deleted")
public class TaskGroupJpaEntity extends BaseLongJpaEntity {

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title")
    private String title;

    @Column(name = "color")
    private String color;

    @Column(name = "position")
    private double position;

    @Column(name = "is_collapsed")
    private boolean collapsed;

    @Column(name = "is_archived")
    private boolean archived;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Column(name = "archived_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID archivedById;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedById;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
