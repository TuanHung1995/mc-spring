package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.TeamMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMemberJpaEntity, UUID> {
}
