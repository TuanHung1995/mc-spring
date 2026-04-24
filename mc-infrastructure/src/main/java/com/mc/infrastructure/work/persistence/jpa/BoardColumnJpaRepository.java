package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.BoardColumnJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * BoardColumnJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface BoardColumnJpaRepository extends JpaRepository<BoardColumnJpaEntity, Long> {

    /** All active (non-deleted) columns for a board, ordered by position. */
    @Query("SELECT c FROM BoardColumnJpaEntity c WHERE c.boardId = :boardId ORDER BY c.position ASC")
    List<BoardColumnJpaEntity> findAllByBoardId(@Param("boardId") Long boardId);

    /** Max position in a board (for append-to-end new column logic). */
    @Query("SELECT MAX(c.position) FROM BoardColumnJpaEntity c WHERE c.boardId = :boardId")
    Double getMaxPositionByBoardId(@Param("boardId") Long boardId);

    /** Position of a specific column (for drag-and-drop calculation). */
    @Query("SELECT c.position FROM BoardColumnJpaEntity c WHERE c.id = :id")
    Double getPositionById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE work_board_columns SET is_deleted = true, updated_at = NOW(), deleted_at = NOW(), deleted_by = :deletedById " +
            "WHERE workspace_id = :workspaceId " +
            "AND is_deleted = false " +
            "LIMIT :batchSize", nativeQuery = true)
    int softDeleteByWorkspaceIdInBatch(@Param("workspaceId") UUID workspaceId, @Param("deletedById") UUID deletedById, @Param("batchSize") int batchSize);

}
