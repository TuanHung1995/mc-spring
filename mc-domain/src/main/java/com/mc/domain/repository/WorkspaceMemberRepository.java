package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.WorkspaceMember;

import java.util.Optional;

public interface WorkspaceMemberRepository {

    Optional<Role> findRoleByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    void save(WorkspaceMember workspaceMember);

    boolean memberExist(Long userId);

}
