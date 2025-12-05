package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface WorkspaceMemberRepository {

    Optional<Role> findRoleByWorkspaceIdAndUserId(Long workspaceId, Long userId);

}
