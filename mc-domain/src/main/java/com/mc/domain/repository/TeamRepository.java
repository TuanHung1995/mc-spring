package com.mc.domain.repository;

import com.mc.domain.model.entity.Team;

import java.util.Optional;

public interface TeamRepository {
    Team save(Team team);
    Optional<Team> findById(Long id);
}
