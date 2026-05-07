package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.ColumnValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ColumnValueRepository — Domain Repository Port (Work Context)
 */
public interface ColumnValueRepository {

    ColumnValue save(ColumnValue columnValue);

    void saveAll(List<ColumnValue> columnValues);

    Optional<ColumnValue> findById(UUID id);

    /** Returns all column values for an item. */
    List<ColumnValue> findByItemId(UUID itemId);

    List<ColumnValue> findByColumnId(Long columnId);

    /** Returns all column values for a board (used for full board load). */
    List<ColumnValue> findByBoardId(UUID boardId);

    int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize);
}
