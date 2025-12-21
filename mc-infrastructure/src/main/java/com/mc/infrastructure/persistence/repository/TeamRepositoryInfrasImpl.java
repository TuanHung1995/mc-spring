package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Team;
import com.mc.domain.repository.TeamRepository;
import com.mc.infrastructure.persistence.mapper.TeamJPAMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamRepositoryInfrasImpl implements TeamRepository {

    private final TeamJPAMapper teamJPAMapper;

    public TeamRepositoryInfrasImpl(TeamJPAMapper teamJPAMapper) {
        this.teamJPAMapper = teamJPAMapper;
    }

    @Override
    public Team save(Team team) {
        return teamJPAMapper.save(team);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJPAMapper.findById(id);
    }

}
