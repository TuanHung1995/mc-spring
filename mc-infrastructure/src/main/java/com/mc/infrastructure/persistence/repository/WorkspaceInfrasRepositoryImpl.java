package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Workspace;
import com.mc.domain.repository.WorkspaceRepository;
import com.mc.infrastructure.persistence.mapper.WorkspaceJPAMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceInfrasRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJPAMapper workspaceJPAMapper;

    public WorkspaceInfrasRepositoryImpl(WorkspaceJPAMapper workspaceJPAMapper) {
        this.workspaceJPAMapper = workspaceJPAMapper;
    }

    @Override
    public void save(Workspace workspace) {
        workspaceJPAMapper.save(workspace);
    }
}
