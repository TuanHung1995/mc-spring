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

    /**
     * Promotes this member to apartment owner.
     *
     * <p><strong>INVARIANT:</strong> Only an ACTIVE member can become an owner.
     * A PENDING invitation holder has not yet committed to the apartment,
     * so granting ownership before acceptance has no product meaning.
     * A REJECTED member has left — they cannot be owner.</p>
     *
     * <p>Called during the AssignOwner use case, AFTER the current owner is
     * demoted. Ownership is always singular: one apartment, one owner.
     * This is enforced at the application service layer by the ordering of calls.</p>
     */
    public void promoteToOwner() {
        if (this.status != MemberStatus.ACTIVE) {
            throw new DomainException(
                    "Only an ACTIVE member can be promoted to owner, current status: " + this.status);
        }
        if (this.owner) {
            throw new DomainException("This member is already the owner");
        }
        this.owner = true;
        this.roleId = 6L;
        markAsModified();
    }

    /**
     * Demotes this member from owner status (removes the owner flag).
     *
     * <p>Called during the AssignOwner use case BEFORE promoting the new owner.
     * Atomically clears the owner flag so there is a brief moment with NO owner
     * (within the same transaction), rather than TWO owners simultaneously.</p>
     *
     * <p><strong>INVARIANT:</strong> You can only demote someone who IS the current owner.
     * Calling this on a non-owner is a logic error that we surface loudly.</p>
     */
    public void demoteFromOwner() {
        if (!this.owner) {
            throw new DomainException("This member is not the current owner");
        }
        this.owner = false;
        this.roleId = 7L;
        markAsModified();
    }

    /**
     * Creates a self-join request (Request to Join flow).
     *
     * <p>Unlike an invitation (created by an owner), a self-join request is
     * initiated by the user themselves. The membership starts as PENDING
     * until an owner approves or rejects it.</p>
     *
     * @param apartmentId The apartment the user wants to join.
     * @param userId      The requesting user's UUID.
     * @param roleId      Default member role ID applied after approval.
     */
    public static ApartmentMember createJoinRequest(UUID apartmentId, UUID userId, Long roleId) {
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
}
