package com.mc.infrastructure.organization.persistence.mapper;

import com.mc.domain.organization.model.entity.ApartmentMember;
import com.mc.domain.organization.model.entity.ApartmentMember.MemberStatus;
import com.mc.infrastructure.organization.persistence.model.ApartmentMemberJpaEntity;
import org.springframework.stereotype.Component;

/**
 * ApartmentMemberPersistenceMapper — Infrastructure Mapper (Organization Context)
 *
 * <p>Translates between the pure domain {@link ApartmentMember} and
 * {@link ApartmentMemberJpaEntity}.</p>
 *
 * <p><strong>STATUS MAPPING:</strong>
 * The DB stores status as MySQL ENUM (ACTIVE, PENDING, REJECTED).
 * We convert String ↔ {@link MemberStatus} enum in this mapper
 * so neither the domain entity nor the JPA entity depends on the other's type system.</p>
 *
 * <p><strong>IS_OWNER MAPPING:</strong>
 * MySQL stores the owner flag as a TINYINT (0/1). The domain uses a primitive boolean.
 * We handle null-safety on the JPA side with a null-safe Boolean.TRUE.equals() check.</p>
 */
@Component
public class ApartmentMemberPersistenceMapper {

    /**
     * Converts a domain {@link ApartmentMember} to its JPA entity for persistence.
     */
    public ApartmentMemberJpaEntity toEntity(ApartmentMember domain) {
        if (domain == null) return null;

        ApartmentMemberJpaEntity entity = new ApartmentMemberJpaEntity();
        entity.setId(domain.getId());
        entity.setApartmentId(domain.getApartmentId());
        entity.setUserId(domain.getUserId());
        entity.setRoleId(domain.getRoleId());
        entity.setOwner(domain.isOwner());
        // Convert domain enum → string for DB storage
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);
        entity.setJoinedAt(domain.getJoinedAt());
        entity.setDeleted(domain.isDeleted());
        return entity;
    }

    /**
     * Reconstitutes a {@link ApartmentMember} domain entity from its JPA entity.
     */
    public ApartmentMember toDomain(ApartmentMemberJpaEntity entity) {
        if (entity == null) return null;

        // Parse string → MemberStatus enum; default to PENDING if unrecognized (defensive)
        MemberStatus status;
        try {
            status = entity.getStatus() != null
                    ? MemberStatus.valueOf(entity.getStatus())
                    : MemberStatus.PENDING;
        } catch (IllegalArgumentException e) {
            status = MemberStatus.PENDING;
        }

        return new ApartmentMember(
                entity.getId(),
                entity.getApartmentId(),
                entity.getUserId(),
                entity.getRoleId(),
                entity.isOwner(),
                status,
                entity.getJoinedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeleted()
        );
    }
}
