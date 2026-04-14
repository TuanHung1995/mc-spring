package com.mc.domain.organization.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMember {

    private UUID id;
    private LocalDateTime joinedAt;
    private UUID roleId;
    private UUID workspaceId;
    private UUID userId;

}
