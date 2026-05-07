package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.work.model.entity.TaskGroup;
import com.mc.domain.work.repository.TaskGroupRepository;
import com.mc.infrastructure.work.persistence.jpa.TaskGroupJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.TaskGroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TaskGroupRepositoryImpl — Persistence Adapter (Work Context)
 */
@Repository
@RequiredArgsConstructor
public class TaskGroupRepositoryImpl implements TaskGroupRepository {

    private final TaskGroupJpaRepository jpaRepository;
    private final TaskGroupPersistenceMapper mapper;

    @Override
    public TaskGroup save(TaskGroup group) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(group)));
    }

    @Override
    public void saveAll(List<TaskGroup> groups) {
        jpaRepository.saveAll(groups.stream().map(mapper::toEntity).collect(Collectors.toList()));
    }

    @Override
    public Optional<TaskGroup> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<TaskGroup> findByIdIncludingDeleted(UUID id) {
        return jpaRepository.findByIdIncludingDeleted(id).map(mapper::toDomain);
    }

    @Override
    public List<TaskGroup> findActiveByBoardId(UUID boardId) {
        return jpaRepository.findActiveByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<TaskGroup> findArchivedByBoardId(UUID boardId) {
        return jpaRepository.findArchivedByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<TaskGroup> findTrashedByBoardId(UUID boardId) {
        return jpaRepository.findTrashedByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<TaskGroup> findByBoardId(UUID boardId) {
        return jpaRepository.findByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Double getMaxPositionByBoardId(UUID boardId) {
        return jpaRepository.getMaxPositionByBoardId(boardId);
    }

    @Override
    public Double getPositionById(UUID groupId) {
        return jpaRepository.getPositionById(groupId);
    }

    @Override
    public void delete(TaskGroup group) {
        // Save soft-delete metadata (deletedBy / deletedAt) first,
        // then let Hibernate @SoftDelete flip the is_deleted flag
        jpaRepository.saveAndFlush(mapper.toEntity(group));
        jpaRepository.delete(mapper.toEntity(group));
    }

    @Override
    @Transactional
    public void permanentDelete(TaskGroup group) {
        jpaRepository.permanentDeleteById(group.getId());
    }

    @Override
    public int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize) {
        return jpaRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }
}
