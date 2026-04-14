package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseLongJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;

/**
 * ColumnValueJpaEntity — Infrastructure JPA Entity (Work Context)
 *
 * <p>Maps to the {@code column_values} table. BIGINT AUTO_INCREMENT PK.
 * Stores item/column/board FKs as scalar Long fields — no @ManyToOne.</p>
 *
 * <p>The {@code type} column mirrors the parent column's BoardColumnType
 * and is stored as a VARCHAR string. The mapper handles String ↔ enum conversion.</p>
 */
@Entity
@Table(name = "work_column_values")
@Getter
@Setter
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
public class ColumnValueJpaEntity extends BaseLongJpaEntity {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "column_id")
    private Long columnId;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "value")
    private String value;

    @Column(name = "text_value")
    private String textValue;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
