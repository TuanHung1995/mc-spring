package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.TaskGroupJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TaskGroupJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface TaskGroupJpaRepository extends JpaRepository<TaskGroupJpaEntity, Long> {

    /** Active (non-deleted, non-archived) groups for a board, ordered by position. */
    @Query("SELECT tg FROM TaskGroupJpaEntity tg " +
           "WHERE tg.boardId = :boardId AND tg.archived = false " +
           "ORDER BY tg.position ASC")
    List<TaskGroupJpaEntity> findActiveByBoardId(@Param("boardId") Long boardId);

    /** Archived groups for a board. */
    @Query("SELECT tg FROM TaskGroupJpaEntity tg " +
           "WHERE tg.boardId = :boardId AND tg.archived = true " +
           "ORDER BY tg.position ASC")
    List<TaskGroupJpaEntity> findArchivedByBoardId(@Param("boardId") Long boardId);

    /**
     * Trashed (soft-deleted) groups — bypasses Hibernate @SoftDelete filter
     * to find rows that are normally invisible.
     */
    @Query(value = "SELECT * FROM work_task_group WHERE board_id = :boardId AND is_deleted = 1 ORDER BY position",
           nativeQuery = true)
    List<TaskGroupJpaEntity> findTrashedByBoardId(@Param("boardId") Long boardId);

    /**
     * Finds a group even if soft-deleted (for restore/permanent-delete flows).
     */
    @Query(value = "SELECT * FROM work_task_group WHERE id = :id AND is_deleted = 1",
           nativeQuery = true)
    Optional<TaskGroupJpaEntity> findByIdIncludingDeleted(@Param("id") Long id);

    /** Max position value in a board (for append-to-end logic). */
    @Query("SELECT MAX(tg.position) FROM TaskGroupJpaEntity tg WHERE tg.boardId = :boardId")
    Double getMaxPositionByBoardId(@Param("boardId") Long boardId);

    /** Position of a specific group (for drag-and-drop calculation). */
    @Query("SELECT tg.position FROM TaskGroupJpaEntity tg WHERE tg.id = :id")
    Double getPositionById(@Param("id") Long id);

    /** Permanently deletes a task group row, bypassing @SoftDelete. */
    @Modifying
    @Query(value = "DELETE FROM work_task_group WHERE id = :id", nativeQuery = true)
    void permanentDeleteById(@Param("id") Long id);
}
