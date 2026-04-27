package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseJpaEntity;
import com.mc.infrastructure.core.persistence.model.BaseLongJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * ItemJpaEntity — Infrastructure JPA Entity (Work Context)
 *
 * <p>Maps to the {@code items} table. BIGINT AUTO_INCREMENT PK.
                                                                                                                       * Stores all FKs as scalar Long fields — no @ManyToOne.</p>
 */
@Entity
@Table(name = "work_items")
@Getter
@Setter
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
public class ItemJpaEntity extends BaseJpaEntity {

    @Column(name = "board_id")
    private UUID boardId;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private double position;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedById;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
