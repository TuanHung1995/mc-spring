package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.work.model.entity.ColumnValue;
import com.mc.domain.work.repository.ColumnValueRepository;
import com.mc.infrastructure.work.persistence.jpa.ColumnValueJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.ColumnValuePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ColumnValueRepositoryImpl — Persistence Adapter (Work Context)
 */
@Repository
@RequiredArgsConstructor
public class ColumnValueRepositoryImpl implements ColumnValueRepository {

    private final ColumnValueJpaRepository jpaRepository;
    private final ColumnValuePersistenceMapper mapper;

    @Override
    public ColumnValue save(ColumnValue columnValue) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(columnValue)));
    }

    @Override
    public void saveAll(List<ColumnValue> columnValues) {
        jpaRepository.saveAll(columnValues.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<ColumnValue> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ColumnValue> findByItemId(UUID itemId) {
        return jpaRepository.findByItemId(itemId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ColumnValue> findByBoardId(UUID boardId) {
        return jpaRepository.findByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize) {
        return jpaRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }
}
