package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.ApartmentMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ApartmentMemberJpaRepository — Spring Data JPA Repository (Organization Context)
 *
 * <p>Uses UUID as the PK type, matching {@code org_apartment_members.id BINARY(16)}.</p>
 */
@Repository
public interface ApartmentMemberJpaRepository extends JpaRepository<ApartmentMemberJpaEntity, UUID> {

    /** Finds a specific membership record for one user in one apartment. */
    Optional<ApartmentMemberJpaEntity> findByApartmentIdAndUserId(UUID apartmentId, UUID userId);

    /** Returns ALL memberships (all statuses) for an apartment. */
    List<ApartmentMemberJpaEntity> findAllByApartmentId(UUID apartmentId);

    /**
     * Returns all memberships for an apartment with a specific status string.
     * Called as: {@code findAllByApartmentIdAndStatus(id, "PENDING")}.
     */
    List<ApartmentMemberJpaEntity> findAllByApartmentIdAndStatus(UUID apartmentId, String status);

    /** Existence check — used to prevent duplicate invitations/join-requests. */
    boolean existsByApartmentIdAndUserId(UUID apartmentId, UUID userId);
}
