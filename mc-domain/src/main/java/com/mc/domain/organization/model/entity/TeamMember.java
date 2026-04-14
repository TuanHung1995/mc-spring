package com.mc.domain.organization.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.core.exception.DomainException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TeamMember — Domain Entity (Organization Bounded Context)
 *
 * <p>Represents a user's membership within a Team. A Team must always have at least
 * one member (its creator). TeamMember is a child entity of the Team aggregate.</p>
 *
 * <p><strong>ROLE NOTE:</strong> {@code roleId} is a Long because the {@code roles} table
 * still uses a Long auto-increment PK. This is a known legacy constraint that will be
 * resolved when roles are migrated to the Organization context's UUID world.</p>
 */
@Getter
public class TeamMember extends BaseDomainEntity {

    // =================================================================
    // STATE
    // =================================================================

    /** The parent Team's ID. */
    private UUID teamId;

    /** The IAM user who is a member of the team. */
    private UUID userId;

    /**
     * The role assigned to this team member.
     * Long because {@code roles} table uses Long PK (legacy — not yet migrated).
     */
    private Long roleId;

    /** When the user joined the team. Set on creation/invitation acceptance. */
    private LocalDateTime joinedAt;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    private TeamMember() {
        super();
    }

    /** Full-arg reconstitution constructor — used only by the persistence mapper. */
    public TeamMember(UUID id, UUID teamId, UUID userId, Long roleId, LocalDateTime joinedAt,
                      LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.teamId = teamId;
        this.userId = userId;
        this.roleId = roleId;
        this.joinedAt = joinedAt;
    }

    // =================================================================
    // FACTORY METHOD
    // =================================================================

    /**
     * Creates the default (owner) membership when a new Team is provisioned.
     *
     * <p>This is called by the application service when a new user is registered —
     * every new user automatically gets a "personal" team, and this creates their
     * founding membership entry.</p>
     *
     * @param userId  The new team owner's UUID.
     * @param teamId  The new team's UUID.
     * @param roleId  The owner-level role ID from the roles table.
     */
    public static TeamMember createDefault(UUID userId, UUID teamId, Long roleId) {
        if (userId == null || teamId == null) {
            throw new DomainException("TeamMember requires both userId and teamId");
        }
        TeamMember member = new TeamMember();
        member.initializeNewEntity(IdUtils.newId());
        member.teamId = teamId;
        member.userId = userId;
        member.roleId = roleId;
        member.joinedAt = LocalDateTime.now();
        return member;
    }
}
