package com.mc.infrastructure.organization.persistence.model;

import com.mc.infrastructure.persistence.model.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "org_teams")
@Getter
@Setter
public class TeamJpaEntity extends BaseJpaEntity {
    
    // Note: Legacy Table uses Long ID. BaseSimpleJpaEntity uses UUID.
    // We cannot extend BaseSimpleJpaEntity if we want to map to existing Long ID table.
    // Creating standalone JPA Entity for Team.

    private String name;
    private String description;
    private String slug;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "deleted_by", columnDefinition = "BINARY(16)")
    private UUID deletedBy;

}
