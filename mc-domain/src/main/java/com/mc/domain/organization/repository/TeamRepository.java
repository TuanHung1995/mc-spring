package com.mc.domain.organization.repository;

import com.mc.domain.organization.model.entity.Team;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository {
    Team save(Team team);
    Optional<Team> findById(UUID id);
    void delete(UUID id);
}
