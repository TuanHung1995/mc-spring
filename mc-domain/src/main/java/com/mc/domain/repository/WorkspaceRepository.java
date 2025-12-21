package com.mc.domain.repository;

import com.mc.domain.model.entity.Workspace;

import java.util.Optional;

public interface WorkspaceRepository {

    Optional<Workspace> findById(Long id);

    void save(Workspace workspace);

}
