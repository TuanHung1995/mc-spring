package com.mc.infrastructure.organization.persistence.repository;

import com.mc.domain.organization.model.entity.Team;
import com.mc.domain.organization.repository.TeamRepository;
import com.mc.infrastructure.organization.persistence.jpa.TeamJpaRepository;
import com.mc.infrastructure.organization.persistence.mapper.TeamPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Component("orgTeamRepository")
public class TeamRepositoryImpl implements TeamRepository {

    @Qualifier("orgTeamJpaRepository")
    private final TeamJpaRepository teamJpaRepository;
    @Qualifier("orgTeamPersistenceMapper")
    private final TeamPersistenceMapper teamPersistenceMapper;

    @Override
    public Team save(Team team) {
        return teamPersistenceMapper.toDomain(
                teamJpaRepository.save(teamPersistenceMapper.toEntity(team))
        );
    }

    @Override
    public Optional<Team> findById(UUID id) {
        return teamJpaRepository.findById(id)
                .map(teamPersistenceMapper::toDomain);
    }

    @Override
    public void delete(UUID id) {
        teamJpaRepository.deleteById(id);
    }
}
