package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface TeamMemberRepository {

    Optional<Role> findRoleByTeamIdAndUserId(Long teamId, Long userId);

}
