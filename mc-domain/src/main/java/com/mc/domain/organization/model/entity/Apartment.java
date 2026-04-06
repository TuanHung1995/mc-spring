package com.mc.domain.organization.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.exception.DomainException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Apartment — Rich Domain Entity (Organization Bounded Context)
 *
 * <p><strong>WHAT IS AN APARTMENT:</strong>
 * An Apartment (in Monday.com terminology: a "Project" or "Space") is a named container
 * for Boards, belonging to one Workspace and one Team. It has an owner (the user who
 * created/manages it) and can have members. The name "Apartment" is this codebase's
 * domain-specific term for this concept.</p>
 *
 * <p><strong>PREVIOUS BUGS FIXED:</strong>
 * <ol>
 *   <li>Had a duplicate {@code private UUID id} field alongside {@code extends BaseDomainEntity},
 *       causing two identity fields. Now removed — identity is inherited.</li>
 *   <li>Had a duplicate {@code private LocalDateTime createdAt} shadowing the base class field.</li>
 *   <li>Was fully anemic ({@code @Getter @Setter @AllArgsConstructor @NoArgsConstructor}) —
 *       any caller could set any field in any combination, bypassing all invariants.</li>
 * </ol>
 * </p>
 */
@Getter
public class Apartment extends BaseDomainEntity {

    // =================================================================
    // STATE — All private, mutated only via behavior methods
    // =================================================================

    /** Display name of the apartment. */
    private String name;

    /** Optional text description shown to members. */
    private String description;

    /**
     * URL to the apartment's background/cover image.
     * Optional — null is valid, the UI falls back to a default color.
     */
    private String backgroundUrl;

    /**
     * The UUID of the user who owns/manages this apartment.
     * The owner has full administrative rights over the apartment.
     */
    private UUID ownerId;

    /**
     * The parent Team. Stored as ID-reference only (aggregate boundary).
     * We do not load the Team object when working on an Apartment.
     */
    private UUID teamId;

    private UUID createdBy;

    /**
     * The parent Workspace. Stored as ID-reference only.
     */
    private UUID workspaceId;

    /** UUID of the user who soft-deleted this apartment (null if active). */
    private UUID deletedBy;

    /** Timestamp of soft deletion. */
    private LocalDateTime deletedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private Apartment() {
        super();
    }

    /**
     * Full-arg constructor for RECONSTITUTION from persistence.
     */
    public Apartment(UUID id, String name, String description, String backgroundUrl,
                     UUID ownerId, UUID teamId, UUID createdBy, UUID workspaceId,
                     UUID deletedBy, LocalDateTime deletedAt,
                     LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.name = name;
        this.description = description;
        this.backgroundUrl = backgroundUrl;
        this.ownerId = ownerId;
        this.teamId = teamId;
        this.createdBy = createdBy;
        this.workspaceId = workspaceId;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates a new Apartment within a Workspace.
     *
     * <p><strong>INVARIANTS:</strong>
     * <ul>
     *   <li>Name must be non-blank — the apartment name is its primary identifier in the UI.</li>
     *   <li>ownerId, teamId, workspaceId must all be present — an apartment without a
     *       parent context has no business meaning.</li>
     * </ul>
     * </p>
     *
     * @param name          Display name.
     * @param description   Optional description.
     * @param backgroundUrl Optional cover image URL.
     * @param ownerId       UUID of the creating user (becomes owner).
     * @param teamId        UUID of the parent Team.
     * @param workspaceId   UUID of the parent Workspace.
     */
    public static Apartment create(String name, String description, String backgroundUrl,
                                   UUID ownerId, UUID teamId, UUID workspaceId) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Apartment name cannot be blank");
        }
        if (ownerId == null) {
            throw new DomainException("Apartment must have an owner");
        }
        if (teamId == null) {
            throw new DomainException("Apartment must belong to a Team");
        }
        if (workspaceId == null) {
            throw new DomainException("Apartment must belong to a Workspace");
        }

        Apartment apt = new Apartment();
        apt.initializeNewEntity(IdUtils.newId());
        apt.name = name.trim();
        apt.description = description;
        apt.backgroundUrl = backgroundUrl;
        apt.ownerId = ownerId;
        apt.createdBy = ownerId;
        apt.teamId = teamId;
        apt.workspaceId = workspaceId;
        apt.deletedBy = null;
        apt.deletedAt = null;
        return apt;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /**
     * Updates the apartment's display details.
     *
     * <p>Only non-null values are applied — this allows partial updates from the
     * UI without requiring the client to resend unchanged fields.
     * The name invariant (non-blank) is still enforced even in updates.</p>
     *
     * @param newName          New display name, or null to leave unchanged.
     * @param newDescription   New description, or null to leave unchanged.
     * @param newBackgroundUrl New background image URL, or null to leave unchanged.
     */
    public void updateDetails(String newName, String newDescription, String newBackgroundUrl) {
        if (newName != null) {
            if (newName.isBlank()) {
                throw new DomainException("Apartment name cannot be blank");
            }
            this.name = newName.trim();
        }
        if (newDescription != null) {
            this.description = newDescription;
        }
        if (newBackgroundUrl != null) {
            this.backgroundUrl = newBackgroundUrl;
        }
        markAsModified();
    }

    /**
     * Transfers ownership to another user.
     *
     * <p>Ownership transfer is an explicit business operation. It is NOT done via
     * a plain setter because it may trigger downstream actions (e.g., an event
     * to notify the new owner). The change is recorded atomically here.</p>
     *
     * @param newOwnerId UUID of the user receiving ownership.
     */
    public void transferOwnership(UUID newOwnerId) {
        if (newOwnerId == null) {
            throw new DomainException("New owner cannot be null");
        }
        this.ownerId = newOwnerId;
        markAsModified();
    }

    /**
     * Soft-deletes this apartment.
     *
     * @param deletedBy UUID of the user performing the deletion.
     */
    public void softDelete(UUID deletedBy) {
        if (this.isDeleted()) {
            throw new DomainException("Apartment is already deleted");
        }
        if (deletedBy == null) {
            throw new DomainException("deletedBy cannot be null for audit trail");
        }
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted();
    }
}
