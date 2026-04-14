package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.TaskGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * TaskGroupRepository — Domain Repository Port (Work Context)
 */
@Component("workTaskGroupRepository")
public interface TaskGroupRepository {

    TaskGroup save(TaskGroup group);

    Optional<TaskGroup> findById(Long id);

    /** Finds a group even if it has been soft-deleted (for restore/permanent-delete flows). */
    Optional<TaskGroup> findByIdIncludingDeleted(Long id);

    /** Returns active (non-deleted, non-archived) groups for a board, ordered by position. */
    List<TaskGroup> findActiveByBoardId(Long boardId);

    /** Returns archived groups for a board. */
    List<TaskGroup> findArchivedByBoardId(Long boardId);

    /** Returns soft-deleted (trashed) groups for a board. */
    List<TaskGroup> findTrashedByBoardId(Long boardId);

    /** Returns the current maximum position value across all groups in a board. */
    Double getMaxPositionByBoardId(Long boardId);

    /** Returns the position of a specific group (used for drag-and-drop calculations). */
    Double getPositionById(Long groupId);

    /** Soft-deletes a group (triggers Hibernate @SoftDelete). */
    void delete(TaskGroup group);

    /** Physically removes a group from the database (bypasses soft-delete). */
    void permanentDelete(TaskGroup group);
}
