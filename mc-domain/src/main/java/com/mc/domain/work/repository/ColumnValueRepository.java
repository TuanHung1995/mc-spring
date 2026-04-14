package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.ColumnValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * ColumnValueRepository — Domain Repository Port (Work Context)
 */
@Component("workColumnValueRepository")
public interface ColumnValueRepository {

    ColumnValue save(ColumnValue columnValue);

    void saveAll(List<ColumnValue> columnValues);

    Optional<ColumnValue> findById(Long id);

    /** Returns all column values for an item. */
    List<ColumnValue> findByItemId(Long itemId);

    /** Returns all column values for a board (used for full board load). */
    List<ColumnValue> findByBoardId(Long boardId);
}
