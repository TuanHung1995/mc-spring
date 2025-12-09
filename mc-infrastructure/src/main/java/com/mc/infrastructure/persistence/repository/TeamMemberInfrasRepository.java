package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.TeamMember;
import com.mc.domain.repository.TeamMemberRepository;
import com.mc.infrastructure.config.cache.caffeine.CacheConfig;
import com.mc.infrastructure.persistence.mapper.TeamMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamMemberInfrasRepository implements TeamMemberRepository {

    private final TeamMemberJPAMapper teamMemberJPAMapper;

    @Override
    @Cacheable(value = CacheConfig.TEAM_ROLE_CACHE, key = "#teamId + '-' + #userId", unless = "#result == null")
    public Optional<Role> findRoleByTeamIdAndUserId(Long teamId, Long userId) {
        return teamMemberJPAMapper.findRoleByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public void save(TeamMember teamMember) {
        teamMemberJPAMapper.save(teamMember);
    }

}
