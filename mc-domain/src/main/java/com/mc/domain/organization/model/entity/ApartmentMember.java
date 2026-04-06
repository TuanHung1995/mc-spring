package com.mc.domain.organization.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.exception.DomainException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ApartmentMember — Domain Entity (Organization Bounded Context)
 *
 * <p>Represents a user's membership within a specific Apartment.
 * Membership has a lifecycle: it starts as PENDING (invitation sent),
 * transitions to ACTIVE (accepted), or REJECTED (declined/removed).</p>
 *
 * <p>The {@code roleId} references the shared {@code roles} table (Long PK, legacy).
 * We use Long here intentionally because the roles table has not been migrated to UUID yet.</p>
 */
@Getter
public class ApartmentMember extends BaseDomainEntity {

    // =================================================================
    // STATE
    // =================================================================

    /** The apartment this membership belongs to. */
    private UUID apartmentId;

    /** The IAM user who is a member. */
    private UUID userId;

    /**
     * The role assigned to this member within the apartment.
     * References the shared roles table (Long PK — not yet migrated to UUID).
     */
    private Long roleId;

    /**
     * Whether this member is the apartment owner.
     * An apartment always has exactly one owner. This flag is set during
     * apartment creation and during ownership transfer.
     */
    private boolean owner;

    /**
     * The membership lifecycle status.
     * PENDING = invitation sent but not accepted.
     * ACTIVE  = member is active.
     * REJECTED = invitation declined or member removed.
     */
    private MemberStatus status;

    /** Timestamp when the user joined the apartment (invitation accepted). */
    private LocalDateTime joinedAt;

    // =================================================================
    // NESTED ENUM — Inline for cohesion; small enough not to warrant a separate file
    // =================================================================

    public enum MemberStatus {
        /** Invitation has been sent but not yet responded to. */
        PENDING,
        /** The user has accepted and is an active member. */
        ACTIVE,
        /** The invitation was declined, or the member was removed. */
        REJECTED
    }

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private ApartmentMember() {
        super();
    }

    /** Full-arg reconstitution constructor — used only by the persistence mapper. */
    public ApartmentMember(UUID id, UUID apartmentId, UUID userId, Long roleId,
                           boolean owner, MemberStatus status, LocalDateTime joinedAt,
                           LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.apartmentId = apartmentId;
        this.userId = userId;
        this.roleId = roleId;
        this.owner = owner;
        this.status = status;
        this.joinedAt = joinedAt;
    }

    // =================================================================
    // FACTORY METHODS
    // =================================================================

    /**
     * Creates a membership record for the apartment owner (the creator).
     *
     * <p>The owner's membership is ACTIVE immediately — no invitation flow is needed.
     * This factory method is called during {@code Apartment.create()} orchestration
     * in the application service.</p>
     *
     * @param apartmentId The new apartment's ID.
     * @param userId      The owner's UUID.
     * @param roleId      The owner-level role ID.
     */
    public static ApartmentMember createOwner(UUID apartmentId, UUID userId, Long roleId) {
        if (apartmentId == null || userId == null) {
            throw new DomainException("ApartmentMember requires both apartmentId and userId");
        }
        ApartmentMember member = new ApartmentMember();
        member.initializeNewEntity(IdUtils.newId());
        member.apartmentId = apartmentId;
        member.userId = userId;
        member.roleId = roleId;
        member.owner = true;
        member.status = MemberStatus.ACTIVE;
        member.joinedAt = LocalDateTime.now();
        return member;
    }

    /**
     * Creates a PENDING invitation for a non-owner member.
     *
     * <p>Members start as {@code PENDING} until they accept the invitation.
     * The joinedAt is null until acceptance.</p>
     */
    public static ApartmentMember createInvitation(UUID apartmentId, UUID userId, Long roleId) {
        if (apartmentId == null || userId == null) {
            throw new DomainException("ApartmentMember requires both apartmentId and userId");
        }
        ApartmentMember member = new ApartmentMember();
        member.initializeNewEntity(IdUtils.newId());
        member.apartmentId = apartmentId;
        member.userId = userId;
        member.roleId = roleId;
        member.owner = false;
        member.status = MemberStatus.PENDING;
        member.joinedAt = null;
        return member;
    }

    // =================================================================
    // BEHAVIOR METHODS
    // =================================================================

    /**
     * Accepts a pending invitation, making the membership ACTIVE.
     *
     * <p>INVARIANT: Only a PENDING membership can be accepted. Accepting an
     * already-active membership would reset the joinedAt timestamp, corrupting audit data.</p>
     */
    public void acceptInvitation() {
        if (this.status != MemberStatus.PENDING) {
            throw new DomainException("Only a PENDING invitation can be accepted");
        }
        this.status = MemberStatus.ACTIVE;
        this.joinedAt = LocalDateTime.now();
        markAsModified();
    }

    /**
     * Rejects or removes a membership.
     *
     * <p>A membership can be rejected while PENDING or removed while ACTIVE.
     * An already REJECTED membership is a no-op that surfaces as an error.</p>
     */
    public void reject() {
        if (this.status == MemberStatus.REJECTED) {
            throw new DomainException("Membership is already rejected");
        }
        this.status = MemberStatus.REJECTED;
        markAsModified();
    }
}
