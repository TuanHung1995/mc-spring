package com.mc.infrastructure.organization.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseSimpleJpaEntity;
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
 * ApartmentMemberJpaEntity — Infrastructure JPA Entity (Organization Context)
 *
 * <p>Maps to the {@code org_apartment_members} table.
 * Extends {@link BaseSimpleJpaEntity} (UUID PK, createdAt/updatedAt, isDeleted)
 * because the table has no createdBy/updatedBy columns.</p>
 *
 * <p><strong>DESIGN NOTE on roleId (Long):</strong>
 * The {@code roles} table still uses a Long auto-increment PK and has not been
 * migrated to UUID. Until that migration happens, we intentionally keep roleId
 * as Long to avoid adding a bridging column. This is a documented technical debt.</p>
 *
 * <p><strong>DESIGN NOTE on status (String):</strong>
 * The DB stores status as a MySQL ENUM. We store it here as a VARCHAR String
 * and let the persistence mapper convert to/from {@link com.mc.domain.organization.model.entity.ApartmentMember.MemberStatus}.
 * This avoids binding the JPA entity to the domain enum.</p>
 */
@Entity
@Table(name = "org_apartment_members")
@Getter
@Setter
public class ApartmentMemberJpaEntity extends BaseSimpleJpaEntity {

    @Column(name = "apartment_id", nullable = false, columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID apartmentId;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID userId;

    /**
     * References the shared roles table (Long PK — legacy, not yet UUID).
     * Nullable because the DB schema allows null role_id.
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * Whether this member is the apartment owner.
     * Stored as TINYINT(1) in MySQL.
     */
    @Column(name = "is_owner")
    private boolean isOwner;

    /**
     * Lifecycle status: ACTIVE | PENDING | REJECTED.
     * Stored as MySQL ENUM, mapped as VARCHAR here — the mapper handles conversion.
     */
    @Column(name = "status")
    private String status;

    /**
     * The timestamp when this member accepted their invitation.
     * Null for PENDING or REJECTED memberships.
     */
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
