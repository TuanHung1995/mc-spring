package com.mc.domain.service;

import com.mc.domain.model.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("workspaceDomainService")
public interface WorkspaceDomainService {

    Optional<Workspace> findById(long id);

}
