package com.mc.domain.service;

import com.mc.domain.model.entity.TaskGroup;

import java.util.List;

public interface TaskGroupDomainService {

    // Create operations
    TaskGroup createGroup(Long boardId, String title, String color, Long userId);

    // Read operations
    TaskGroup getGroupById(Long groupId);
    List<TaskGroup> getGroupsByBoardId(Long boardId);

    // Update operations
    TaskGroup updateTaskGroup(Long groupId, String newTitle, String newColor);
    TaskGroup reorderGroup(Long targetGroup, Long prevItemId, Long nextItemId);

    // Delete operations
    void deleteGroup(Long groupId, Long userId);

    // Archive operations
    TaskGroup archiveGroup(Long groupId, Long userId);
    TaskGroup unarchiveGroup(Long groupId, Long userId);
    List<TaskGroup> getArchivedGroupsByBoardId(Long boardId);

}
