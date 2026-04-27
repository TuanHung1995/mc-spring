package com.mc.domain.organization.model.entity;

import com.mc.domain.core.exception.DomainException;
import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.organization.model.enums.WorkspaceStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Team extends BaseDomainEntity {

    private String name;
    private String description;
    private String slug;
    private UUID createdBy;
    private LocalDateTime deletedAt;
    private UUID deletedBy;

    public Team(UUID id, String name, String description, String slug, UUID createdBy, LocalDateTime deletedAt, UUID deletedBy
    , LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.createdBy = createdBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

    public Team() {
        super();
    }

    // Factory method for creating default team
    public static Team createDefault(String fullName, UUID ownerId) {
        Team team = new Team();
        team.initializeNewEntity(IdUtils.newId());
        team.createdBy = ownerId;
        team.name = fullName + "'s Team";
        team.slug = "team-" + ownerId;
        return team;
    }

    public void softDelete(UUID deletedBy) {
        if (this.isDeleted()) {
            throw new DomainException("Workspace is already deleted");
        }
        if (deletedBy == null) {
            throw new DomainException("deletedBy cannot be null for audit trail");
        }
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
        markAsDeleted(); // sets the base `deleted = true` flag for filtering
    }
}
