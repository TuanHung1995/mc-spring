package com.mc.domain.repository;

import com.mc.domain.model.entity.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    Optional<TaskGroup> findById(Long id);
    TaskGroup save(TaskGroup group);
    void delete(TaskGroup group);
    
    List<TaskGroup> findByBoardId(Long boardId);
    List<TaskGroup> findArchivedGroupsByBoardId(Long boardId);
    List<TaskGroup> findTrashedGroupsByBoardId(Long boardId);
    
    Optional<TaskGroup> findByIdIncludingDeleted(Long id);
    void permanentDelete(TaskGroup group);
    
    Double getPosition(Long groupId);
    Double getMaxPositionByBoardId(Long boardId);

    Optional<Long> findBoardIdByGroupId(Long groupId);

}
