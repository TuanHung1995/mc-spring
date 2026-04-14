package com.mc.infrastructure.organization.persistence.jpa;

import com.mc.infrastructure.organization.persistence.model.TeamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Component("orgTeamJpaRepository")
public interface TeamJpaRepository extends JpaRepository<TeamJpaEntity, UUID> {
}
