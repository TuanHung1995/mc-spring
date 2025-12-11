package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.WorkspaceMember;
import com.mc.domain.repository.WorkspaceMemberRepository;
import com.mc.infrastructure.persistence.mapper.WorkspaceMemberJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberInfrasRepository implements WorkspaceMemberRepository {

    private final WorkspaceMemberJPAMapper workspaceMemberJPAMapper;

    @Override
    @Cacheable(value = "workspaceRoles", key = "#workspaceId + '-' + #userId")
    public Optional<Role> findRoleByWorkspaceIdAndUserId(Long workspaceId, Long userId) {
        return workspaceMemberJPAMapper.findRoleByWorkspaceIdAndUserId(workspaceId, userId);
    }

    @Override
    public void save(WorkspaceMember workspaceMember) {
        workspaceMemberJPAMapper.save(workspaceMember);
    }

}
