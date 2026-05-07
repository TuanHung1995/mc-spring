package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.TaskGroupJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TaskGroupJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface TaskGroupJpaRepository extends JpaRepository<TaskGroupJpaEntity, UUID> {

    /** Active (non-deleted, non-archived) groups for a board, ordered by position. */
    @Query("SELECT tg FROM TaskGroupJpaEntity tg " +
           "WHERE tg.boardId = :boardId AND tg.archived = false " +
           "ORDER BY tg.position ASC")
    List<TaskGroupJpaEntity> findActiveByBoardId(@Param("boardId") UUID boardId);

    /** Archived groups for a board. */
    @Query("SELECT tg FROM TaskGroupJpaEntity tg " +
           "WHERE tg.boardId = :boardId AND tg.archived = true " +
           "ORDER BY tg.position ASC")
    List<TaskGroupJpaEntity> findArchivedByBoardId(@Param("boardId") UUID boardId);

    /**
     * Trashed (soft-deleted) groups — bypasses Hibernate @SoftDelete filter
     * to find rows that are normally invisible.
     */
    @Query(value = "SELECT * FROM work_task_groups WHERE board_id = :boardId AND is_deleted = 1 ORDER BY position",
           nativeQuery = true)
    List<TaskGroupJpaEntity> findTrashedByBoardId(@Param("boardId") UUID boardId);

    /**
     * Finds a group even if soft-deleted (for restore/permanent-delete flows).
     */
    @Query(value = "SELECT * FROM work_task_groups WHERE id = :id AND is_deleted = 1",
           nativeQuery = true)
    Optional<TaskGroupJpaEntity> findByIdIncludingDeleted(@Param("id") UUID id);

    @Query(value = "SELECT * FROM work_task_groups WHERE id = :id", nativeQuery = true)
    List<TaskGroupJpaEntity> findByBoardId(@Param("boardId") UUID boardId);

    /** Max position value in a board (for append-to-end logic). */
    @Query("SELECT MAX(tg.position) FROM TaskGroupJpaEntity tg WHERE tg.boardId = :boardId")
    Double getMaxPositionByBoardId(@Param("boardId") UUID boardId);

    /** Position of a specific group (for drag-and-drop calculation). */
    @Query("SELECT tg.position FROM TaskGroupJpaEntity tg WHERE tg.id = :id")
    Double getPositionById(@Param("id") UUID id);

    /** Permanently deletes a task group row, bypassing @SoftDelete. */
    @Modifying
    @Query(value = "DELETE FROM work_task_groups WHERE id = :id", nativeQuery = true)
    void permanentDeleteById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE work_task_groups SET is_deleted = true, updated_at = NOW(), deleted_at = NOW(), deleted_by = :deletedById " +
            "WHERE workspace_id = :workspaceId " +
            "AND is_deleted = false " +
            "LIMIT :batchSize", nativeQuery = true)
    int softDeleteByWorkspaceIdInBatch(@Param("workspaceId") UUID workspaceId, @Param("deletedById") UUID deletedById, @Param("batchSize") int batchSize);
}
