package com.mc.domain.organization.repository;

import com.mc.domain.organization.model.entity.ApartmentMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ApartmentMemberRepository — Domain Repository Port (Organization Context)
 *
 * <p>Outbound port for ApartmentMember persistence, keyed entirely on UUID.
 * Maps to the {@code org_apartment_members} table.</p>
 *
 * <p>The implementation lives in mc-infrastructure as {@code ApartmentMemberRepositoryImpl}.</p>
 */
public interface ApartmentMemberRepository {

    /** Persists a new or updated membership record. Returns the saved state. */
    ApartmentMember save(ApartmentMember member);

    /** Finds a membership by its primary key UUID. */
    Optional<ApartmentMember> findById(UUID id);

    /**
     * Finds the membership of a specific user within a specific apartment.
     * Used to verify membership, check role, and prevent duplicate memberships.
     */
    Optional<ApartmentMember> findByApartmentIdAndUserId(UUID apartmentId, UUID userId);

    /** Returns all memberships for an apartment (all statuses). */
    List<ApartmentMember> findAllByApartmentId(UUID apartmentId);

    /**
     * Returns all PENDING memberships for an apartment.
     * Used by the owner to see pending join requests and invitations.
     */
    List<ApartmentMember> findPendingByApartmentId(UUID apartmentId);

    /** Returns all ACTIVE members of an apartment. */
    List<ApartmentMember> findActiveByApartmentId(UUID apartmentId);

    /**
     * Checks if a user already has ANY membership record (any status) in an apartment.
     * Used to prevent duplicate invitations and duplicate join requests.
     */
    boolean existsByApartmentIdAndUserId(UUID apartmentId, UUID userId);

    /** Soft-deletes a membership record by its UUID. */
    void delete(UUID id);
}
