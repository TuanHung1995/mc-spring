package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.work.model.entity.BoardColumn;
import com.mc.domain.work.repository.BoardColumnRepository;
import com.mc.infrastructure.work.persistence.jpa.BoardColumnJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.BoardColumnPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BoardColumnRepositoryImpl — Persistence Adapter (Work Context)
 */
@Repository
@RequiredArgsConstructor
public class BoardColumnRepositoryImpl implements BoardColumnRepository {

    private final BoardColumnJpaRepository jpaRepository;
    private final BoardColumnPersistenceMapper mapper;

    @Override
    public BoardColumn save(BoardColumn column) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(column)));
    }

    @Override
    public Optional<BoardColumn> findById(Long columnId) {
        return jpaRepository.findById(columnId).map(mapper::toDomain);
    }

    @Override
    public List<BoardColumn> findAllByBoardId(Long boardId) {
        return jpaRepository.findAllByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Double getMaxPositionByBoardId(Long boardId) {
        return jpaRepository.getMaxPositionByBoardId(boardId);
    }

    @Override
    public Double getPositionById(Long columnId) {
        return jpaRepository.getPositionById(columnId);
    }

    @Override
    public void delete(BoardColumn column) {
        jpaRepository.saveAndFlush(mapper.toEntity(column));
        jpaRepository.delete(mapper.toEntity(column));
    }

    @Override
    public int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize) {
        return jpaRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }
}
