package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.work.model.entity.Board;
import com.mc.domain.work.repository.BoardRepository;
import com.mc.infrastructure.work.persistence.jpa.BoardJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.BoardPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BoardRepositoryImpl — Persistence Adapter (Work Context)
 *
 * <p>Implements the domain port {@link BoardRepository} using Spring Data JPA.
 * This adapter is the <em>only</em> class in the application that knows about
 * {@link com.mc.infrastructure.work.persistence.model.BoardJpaEntity}.</p>
 */
@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository jpaRepository;
    private final BoardPersistenceMapper mapper;

    @Override
    public Board save(Board board) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(board)));
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return jpaRepository.findById(boardId).map(mapper::toDomain);
    }

    @Override
    public List<Board> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Board> findAllByWorkspaceId(UUID workspaceId) {
        return jpaRepository.findAllByWorkspaceId(workspaceId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Board> findAllTrashedByUserId(Long userId) {
        return jpaRepository.findAllTrashedByUserId(userId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Board delete(Board board) {
        // Persist the soft-delete fields before Hibernate applies @SoftDelete flag
        return mapper.toDomain(jpaRepository.saveAndFlush(mapper.toEntity(board)));
    }

    @Override
    @Transactional
    public void deletePhysical(Long boardId) {
        jpaRepository.deletePhysical(boardId);
    }

    @Override
    @Transactional
    public void restore(Long boardId) {
        jpaRepository.restore(boardId);
    }

    @Override
    public int softDeleteByWorkspaceIdInBatch(UUID workspaceID, UUID deletedById, int batchSize) {
        return jpaRepository.softDeleteByWorkspaceIdInBatch(workspaceID, deletedById, batchSize);
    }

    @Override
    public Long countByWorkspaceId(UUID workspaceId) {
        return jpaRepository.countByWorkspaceId(workspaceId);
    }

}
