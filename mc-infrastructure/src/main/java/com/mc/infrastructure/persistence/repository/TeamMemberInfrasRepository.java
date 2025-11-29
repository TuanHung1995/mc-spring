package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.repository.TeamMemberRepository;
import com.mc.infrastructure.persistence.mapper.TeamMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamMemberInfrasRepository implements TeamMemberRepository {

    private final TeamMemberJPAMapper teamMemberJPAMapper;

    @Override
    public Optional<Role> findRoleByTeamIdAndUserId(Long teamId, Long userId) {
        return teamMemberJPAMapper.findRoleByTeamIdAndUserId(teamId, userId);
    }

}
