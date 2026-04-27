package com.mc.infrastructure.organization.persistence.repository;

import com.mc.domain.organization.model.entity.Workspace;
import com.mc.domain.organization.repository.WorkspaceRepository;
import com.mc.infrastructure.organization.persistence.jpa.WorkspaceJpaRepository;
import com.mc.infrastructure.organization.persistence.mapper.WorkspacePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * WorkspaceRepositoryImpl — Persistence Adapter (Organization Context)
 *
 * <p>Implements the domain's {@link WorkspaceRepository} port using Spring Data JPA.
 * This class is the only place that knows about {@code WorkspaceJpaEntity} — the domain
 * service and application service work exclusively with the clean {@link Workspace} model.</p>
 */
@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final WorkspacePersistenceMapper workspacePersistenceMapper;

    @Override
    public Workspace save(Workspace workspace) {
        return workspacePersistenceMapper.toDomain(
                workspaceJpaRepository.save(workspacePersistenceMapper.toEntity(workspace))
        );
    }

    @Override
    public Optional<Workspace> findById(UUID id) {
        return workspaceJpaRepository.findById(id)
                .map(workspacePersistenceMapper::toDomain);
    }

    @Override
    public List<Workspace> findAllByTeamId(UUID teamId) {
        return workspaceJpaRepository.findAllByTeamId(teamId).stream()
                .map(workspacePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        workspaceJpaRepository.deleteById(id);
    }

    @Override
    public List<UUID> findIdsByTeamId(UUID teamId) {
        return workspaceJpaRepository.findIdsByTeamId(teamId);
    }

    @Override
    public void softDeleteByIds(UUID teamId, UUID deletedBy) {
        workspaceJpaRepository.softDeleteByIds(teamId, deletedBy);
    }
}
