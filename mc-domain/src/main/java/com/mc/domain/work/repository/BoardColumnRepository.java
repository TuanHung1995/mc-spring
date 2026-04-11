package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.BoardColumn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * BoardColumnRepository — Domain Repository Port (Work Context)
 */
@Component("workBoardColumnRepository")
public interface BoardColumnRepository {

    BoardColumn save(BoardColumn column);

    Optional<BoardColumn> findById(Long columnId);

    /** Returns all active (non-deleted) columns for a board, ordered by position. */
    List<BoardColumn> findAllByBoardId(Long boardId);

    /** Returns the max position among all columns on a board (for append-to-end logic). */
    Double getMaxPositionByBoardId(Long boardId);

    /** Returns the position of a specific column (for reorder calculations). */
    Double getPositionById(Long columnId);

    /** Soft-deletes a column. */
    void delete(BoardColumn column);
}
