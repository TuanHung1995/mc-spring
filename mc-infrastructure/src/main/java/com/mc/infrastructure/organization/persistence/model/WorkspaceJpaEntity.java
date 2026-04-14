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
 * WorkspaceJpaEntity — Infrastructure JPA Entity (Organization Context)
 *
 * <p>Maps to the {@code org_workspaces} table.
 * Extends {@link BaseJpaEntity} which provides:
 * <ul>
 *   <li>UUID id (BINARY 16)</li>
 *   <li>createdAt / updatedAt via Spring Data Auditing</li>
 *   <li>createdBy / updatedBy via Spring Data Auditing (@CurrentAuditor)</li>
 *   <li>isDeleted soft-delete flag</li>
 * </ul>
 * </p>
 *
 * <p>The {@code status} column stores the enum name as VARCHAR.
 * The mapper converts between {@code WorkspaceStatus} enum and String.</p>
 */
@Entity
@Table(name = "org_workspaces")
@Getter
@Setter
public class WorkspaceJpaEntity extends BaseJpaEntity {

    private String name;

    /**
     * Stored as VARCHAR in DB. The persistence mapper converts to/from
     * {@link com.mc.domain.organization.model.enums.WorkspaceStatus}.
     */
    private String status;

    @Column(name = "team_id", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID teamId;

    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
