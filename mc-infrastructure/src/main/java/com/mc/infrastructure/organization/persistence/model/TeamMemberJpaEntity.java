package com.mc.infrastructure.organization.persistence.model;

import com.mc.infrastructure.core.persistence.model.BaseSimpleJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "org_team_members")
@Getter
@Setter
public class TeamMemberJpaEntity extends BaseSimpleJpaEntity {

    private UUID userId;
    private UUID teamId;
    private Long roleId;
    private LocalDateTime joinedAt;
    private LocalDateTime deletedAt;
}
