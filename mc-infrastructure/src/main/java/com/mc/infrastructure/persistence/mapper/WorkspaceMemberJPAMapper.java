package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.WorkspaceMember;
import com.mc.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkspaceMemberJPAMapper extends JpaRepository<WorkspaceMember, Long> {

    @Query("SELECT wm.role FROM WorkspaceMember wm WHERE wm.workspace.id = :workspaceId AND wm.user.id = :userId")
    Optional<Role> findRoleByWorkspaceIdAndUserId(@Param("workspaceId") Long workspaceId, @Param("userId") Long userId);
}
