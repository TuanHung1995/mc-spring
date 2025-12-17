package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Workspace;
import com.mc.domain.repository.WorkspaceRepository;
import com.mc.domain.service.WorkspaceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceDomainServiceImpl implements WorkspaceDomainService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public Optional<Workspace> findById(long id) {
        return workspaceRepository.findById(id);
    }

}
