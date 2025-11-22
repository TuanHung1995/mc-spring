package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceJPAMapper extends JpaRepository<Workspace, Long> {
}
