package com.mc.infrastructure.organization.persistence.repository;

import com.mc.domain.organization.model.entity.TeamMember;
import com.mc.domain.organization.repository.TeamMemberRepository;
import com.mc.infrastructure.organization.persistence.jpa.TeamMemberJpaRepository;
import com.mc.infrastructure.organization.persistence.mapper.TeamMemberPersistenceMapper;
import com.mc.infrastructure.organization.persistence.model.TeamMemberJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamMemberRepositoryImpl implements TeamMemberRepository {

    private final TeamMemberJpaRepository teamMemberJpaRepository;
    private final TeamMemberPersistenceMapper teamMemberPersistenceMapper;

    @Override
    public void addTeamMember(TeamMember teamMember) {
        log.info("Adding team member with userId: {}, teamId: {}, roleId: {}", teamMember.getUserId(), teamMember.getTeamId(), teamMember.getRoleId());
        // Implement the logic to add a team member to the database
        // For example, you might want to create a new TeamMemberJpaEntity and save it
        TeamMemberJpaEntity teamMemberJpaEntity = teamMemberPersistenceMapper.toEntity(teamMember);
        teamMemberJpaRepository.save(teamMemberJpaEntity);
    }

}
