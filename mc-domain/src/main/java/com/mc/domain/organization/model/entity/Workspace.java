package com.mc.domain.organization.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.core.exception.DomainException;
import com.mc.domain.organization.model.enums.WorkspaceStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Workspace — Rich Domain Entity (Organization Bounded Context)
 *
 * <p><strong>WHAT IS A WORKSPACE:</strong>
 * A Workspace is a logical container owned by a Team. It groups Apartments (project boards)
 * together. In Monday.com's product model, a Team has one or more Workspaces, each of which
 * contains multiple Apartments (board spaces). The Workspace is the primary navigation boundary.</p>
 *
 * <p><strong>AGGREGATE BOUNDARY:</strong>
 * Workspace is NOT the Aggregate Root of its children (Apartments). Each Apartment is its own
 * entity and references Workspace only by ID. This prevents loading the entire workspace graph
 * when working on a single apartment.</p>
 *
 * <p><strong>NO JPA ANNOTATIONS — BY DESIGN:</strong>
 * This is a pure domain object. All persistence concerns live in {@code WorkspaceJpaEntity}
 * inside {@code mc-infrastructure}.</p>
 */
@Getter
public class Workspace extends BaseDomainEntity {

    // =================================================================
    // STATE — All private, mutated only via behavior methods
    // =================================================================

    /**
     * The ID of the parent Team this workspace belongs to.
     * Stored as a reference ID (not the full Team object) to respect aggregate boundaries.
     */
    private UUID teamId;

    /** Display name of the workspace. */
    private String name;

    private UUID createdBy;

    /**
     * Lifecycle status of the workspace.
     * Using an enum guarantees only valid states are representable in the domain.
     */
    private WorkspaceStatus status;

    /** UUID of the user who deleted this workspace (null if not deleted). */
    private UUID deletedBy;

    /** Timestamp of soft deletion. */
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private Workspace() {
        super();
    }

    /**
     * Full-arg constructor for RECONSTITUTION from persistence.
     * No validation — we trust data that was previously validated on write.
     */
    public Workspace(UUID id, UUID teamId, String name, WorkspaceStatus status,
                     UUID createdBy,UUID deletedBy, LocalDateTime deletedAt,
                     LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.teamId = teamId;
        this.name = name;
        this.status = status;
        this.createdBy = createdBy;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new Workspace within a Team.
     *
     * <p><strong>INVARIANTS ENFORCED:</strong>
     * <ul>
     *   <li>Name cannot be blank — a nameless workspace is unusable in the UI.</li>
     *   <li>teamId cannot be null — a workspace without a team is an orphan.</li>
     * </ul>
     * New workspaces always start as {@code ACTIVE}. There is no business case
     * for creating a workspace in a DELETED state.</p>
     *
     * @param name      The display name of the workspace.
     * @param creatorId The UUID of the IAM User creating this workspace.
     * @param teamId    The UUID of the Team this workspace belongs to.
     */
    public static Workspace create(String name, UUID creatorId, UUID teamId) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Workspace name cannot be blank");
        }
        if (teamId == null) {
            throw new DomainException("Workspace must belong to a Team");
        }
        if (creatorId == null) {
            throw new DomainException("Workspace creator cannot be null");
        }

        Workspace ws = new Workspace();
        ws.initializeNewEntity(IdUtils.newId());
        ws.name = name.trim();
        ws.teamId = teamId;
        ws.createdBy = creatorId;
        ws.status = WorkspaceStatus.ACTIVE;
        ws.deletedBy = null;
        ws.deletedAt = null;
        return ws;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /**
     * Renames the workspace.
     *
     * <p>Only active workspaces can be renamed. Renaming a deleted workspace is
     * nonsensical and likely a UI bug — we fail loudly here to surface the problem.</p>
     *
     * @param newName The new display name.
     */
    public void rename(String newName) {
        if (this.status == WorkspaceStatus.DELETED) {
            throw new DomainException("Cannot rename a deleted workspace");
        }
        if (newName == null || newName.isBlank()) {
            throw new DomainException("Workspace name cannot be blank");
        }
        this.name = newName.trim();
        markAsModified();
    }

    /**
     * Soft-deletes this workspace.
     *
     * <p><strong>INVARIANT:</strong> A workspace that is already deleted cannot be deleted again.
     * This prevents stale data overwrites on the deletedBy/deletedAt audit fields.</p>
     *
     * @param deletedBy UUID of the user performing the deletion.
     */
    public void softDelete(UUID deletedBy) {
        if (this.status == WorkspaceStatus.DELETED) {
            throw new DomainException("Workspace is already deleted");
        }
        if (deletedBy == null) {
            throw new DomainException("deletedBy cannot be null for audit trail");
        }
        this.status = WorkspaceStatus.DELETED;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted(); // sets the base `deleted = true` flag for filtering
    }

    /** Convenience: checks if this workspace is usable. */
    public boolean isActive() {
        return this.status == WorkspaceStatus.ACTIVE;
    }
}
