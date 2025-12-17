package com.mc.domain.service;

import com.mc.domain.model.entity.Workspace;

import java.util.Optional;

public interface WorkspaceDomainService {

    Optional<Workspace> findById(long id);

}
