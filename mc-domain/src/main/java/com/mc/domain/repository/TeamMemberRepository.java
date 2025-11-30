package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.TeamMember;

import java.util.Optional;

public interface TeamMemberRepository {

    void save(TeamMember teamMember);

    Optional<Role> findRoleByTeamIdAndUserId(Long teamId, Long userId);

}
