package com.mc.domain.organization.model.entity;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
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
}
