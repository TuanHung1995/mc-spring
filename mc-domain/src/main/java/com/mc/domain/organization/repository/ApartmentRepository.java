package com.mc.domain.organization.repository;

import com.mc.domain.organization.model.entity.Apartment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ApartmentRepository — Domain Repository Port (Organization Context)
 *
 * <p>Outbound port defining persistence operations for Apartments.
 * All IDs are UUID to match the {@code org_apartments} table schema (BINARY(16) PK).</p>
 */
public interface ApartmentRepository {

    /** Persists a new or updated Apartment. Returns the saved state. */
    Apartment save(Apartment apartment);

    /** Finds an Apartment by its UUID primary key. */
    Optional<Apartment> findById(UUID id);

    /**
     * Finds all non-deleted Apartments belonging to a Workspace.
     *
     * @param workspaceId UUID of the parent Workspace.
     */
    List<Apartment> findAllByWorkspaceId(UUID workspaceId);

    /** Deletes an Apartment by UUID. Prefer soft-delete via the domain entity. */
    void delete(UUID id);

    void softDeleteByWorkspaceId(UUID workspaceId, UUID deletedBy);
}
