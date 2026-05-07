package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.TaskGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TaskGroupRepository — Domain Repository Port (Work Context)
 */
public interface TaskGroupRepository {

    TaskGroup save(TaskGroup group);

    void saveAll(List<TaskGroup> groups);

    Optional<TaskGroup> findById(UUID id);

    /** Finds a group even if it has been soft-deleted (for restore/permanent-delete flows). */
    Optional<TaskGroup> findByIdIncludingDeleted(UUID id);

    /** Returns active (non-deleted, non-archived) groups for a board, ordered by position. */
    List<TaskGroup> findActiveByBoardId(UUID boardId);

    /** Returns archived groups for a board. */
    List<TaskGroup> findArchivedByBoardId(UUID boardId);

    /** Returns soft-deleted (trashed) groups for a board. */
    List<TaskGroup> findTrashedByBoardId(UUID boardId);

    List<TaskGroup> findByBoardId(UUID boardId);

    /** Returns the current maximum position value across all groups in a board. */
    Double getMaxPositionByBoardId(UUID boardId);

    /** Returns the position of a specific group (used for drag-and-drop calculations). */
    Double getPositionById(UUID groupId);

    /** Soft-deletes a group (triggers Hibernate @SoftDelete). */
    void delete(TaskGroup group);

    /** Physically removes a group from the database (bypasses soft-delete). */
    void permanentDelete(TaskGroup group);

    int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize);
}
