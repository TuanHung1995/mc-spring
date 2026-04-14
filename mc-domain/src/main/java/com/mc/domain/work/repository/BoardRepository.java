package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.Board;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * BoardRepository — Domain Repository Port (Work Context)
 *
 * <p>Outbound port for Board persistence. Implementation lives in mc-infrastructure.</p>
 */
@Component("workBoardRepository")
public interface BoardRepository {

    Board save(Board board);

    Optional<Board> findById(Long boardId);

    /** Returns all boards where ${userId} is a member. */
    List<Board> findAllByUserId(UUID userId);

    /** Returns all boards soft-deleted by the given user (for Trash UI). */
    List<Board> findAllTrashedByUserId(Long userId);

    /** Soft-deletes a board (sets is_deleted = 1). The board's trash() must be called first. */
    Board delete(Board board);

    /** Permanently removes a board from the database (bypasses Hibernate @SoftDelete). */
    void deletePhysical(Long boardId);

    /** Restores a soft-deleted board (sets is_deleted = 0). */
    void restore(Long boardId);
}
