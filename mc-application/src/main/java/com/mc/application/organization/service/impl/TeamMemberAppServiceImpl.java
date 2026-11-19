package com.mc.application.organization.service.impl;

import com.mc.application.organization.service.TeamMemberAppService;
import com.mc.domain.organization.model.entity.TeamMember;
import com.mc.domain.organization.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamMemberAppServiceImpl implements TeamMemberAppService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public void addTeamMember(UUID userId, UUID teamId, Long roleId) {
        log.info("Adding team member with userId: {}, teamId: {}, roleId: {}", userId, teamId, roleId);
        TeamMember teamMember = TeamMember.createDefault(userId, teamId, roleId);
        teamMemberRepository.addTeamMember(teamMember);
    }

}
