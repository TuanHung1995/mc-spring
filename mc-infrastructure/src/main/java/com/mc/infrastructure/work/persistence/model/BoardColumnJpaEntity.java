package com.mc.infrastructure.work.persistence.model;

import com.mc.infrastructure.persistence.model.BaseLongJpaEntity;
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
 * BoardColumnJpaEntity — Infrastructure JPA Entity (Work Context)
 *
 * <p>Maps to the {@code board_columns} table. BIGINT AUTO_INCREMENT PK.</p>
 */
@Entity
@Table(name = "work_board_columns")
@Getter
@Setter
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
public class BoardColumnJpaEntity extends BaseLongJpaEntity {

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title")
    private String title;

    /** Stored as a VARCHAR string; mapper converts to/from BoardColumnType enum. */
    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private double position;

    @Column(name = "width")
    private int width;

    @Column(name = "is_hidden")
    private boolean hidden;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedById;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
