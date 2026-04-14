package com.mc.infrastructure.organization.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ApartmentJpaEntity — Infrastructure JPA Entity (Organization Context)
 *
 * <p>Maps to the {@code org_apartments} table.
 * Extends {@link BaseJpaEntity} which provides UUID id (BINARY 16),
 * Spring Data Auditing, and the soft-delete flag.</p>
 *
 * <p><strong>PREVIOUS BUGS FIXED:</strong>
 * <ol>
 *   <li>Used {@code @GeneratedValue(IDENTITY)} with a Long PK — but the table
 *       has {@code BINARY(16) primary key}. This would have caused a type mismatch.</li>
 *   <li>Was missing {@code deletedBy}, {@code deletedAt}, {@code updatedAt} columns
 *       which exist in the DB schema but were never mapped.</li>
 * </ol>
 * </p>
 */
@Entity
@Table(name = "org_apartments")
@Getter
@Setter
public class ApartmentJpaEntity extends BaseJpaEntity {

    private String name;
    private String description;

    @Column(name = "background_url")
    private String backgroundUrl;

    @Column(name = "owner_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID ownerId;

    @Column(name = "team_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID teamId;

    @Column(name = "workspace_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID workspaceId;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
