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
public interface BoardRepository {

    Board save(Board board);

    Optional<Board> findById(UUID boardId);

    /** Returns all boards where ${userId} is a member. */
    List<Board> findAllByUserId(UUID userId);

    /** Returns all boards belonging to a specific workspace. */
    List<Board> findAllByWorkspaceId(UUID workspaceId);

    /** Returns all boards soft-deleted by the given user (for Trash UI). */
    List<Board> findAllTrashedByUserId(UUID userId);

    /** Soft-deletes a board (sets is_deleted = 1). The board's trash() must be called first. */
    Board delete(Board board);

    /** Permanently removes a board from the database (bypasses Hibernate @SoftDelete). */
    void deletePhysical(UUID boardId);

    /** Restores a soft-deleted board (sets is_deleted = 0). */
    void restore(UUID boardId);

    int softDeleteByWorkspaceIdInBatch(UUID workspaceID, UUID deletedById, int batchSize);

    Long countByWorkspaceId(UUID workspaceId);

}
