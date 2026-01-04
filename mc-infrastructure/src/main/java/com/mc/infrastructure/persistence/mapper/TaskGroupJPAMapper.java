package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskGroupJPAMapper extends JpaRepository<TaskGroup, Long> {

    @Query("SELECT tg.position FROM TaskGroup tg WHERE tg.id = :groupId")
    Double getPosition(Long groupId);

    @Query("SELECT MAX(tg.position) FROM TaskGroup tg WHERE tg.board.id = :boardId")
    Double getMaxPositionByBoardId(Long boardId);

    @Query("SELECT tg FROM TaskGroup tg WHERE tg.board.id = :boardId ORDER BY tg.position")
    List<TaskGroup> findByBoardId(Long boardId);

    @Query("SELECT tg FROM TaskGroup tg WHERE tg.board.id = :boardId AND tg.isArchived = true ORDER BY tg.position")
    List<TaskGroup> findArchivedGroupsByBoardId(Long boardId);

    @Query(value = "SELECT * FROM task_groups WHERE board_id = :boardId AND is_deleted = 1 ORDER BY position", nativeQuery = true)
    List<TaskGroup> findTrashedGroupsByBoardId(Long boardId);

    @Query(value = "SELECT * FROM task_groups WHERE id = :id AND is_deleted = 1", nativeQuery = true)
    Optional<TaskGroup> findByIdIncludingDeleted(Long id);

    @Query("SELECT i.board.id FROM TaskGroup i WHERE i.id = :groupId")
    Optional<Long> findBoardIdByGroupId(Long groupId);

}
