package com.mc.domain.service;

import com.mc.domain.model.entity.TaskGroup;

public interface TaskGroupDomainService {

    TaskGroup reorderGroup(Long targetGroup, Long prevItemId, Long nextItemId);

    /**
     * Update task group details
     * @param groupId: ID of the group to be updated
     * @param newTitle: New title (optional)
     * @param newColor: New color (optional)
     * @return Updated TaskGroup entity
     */
    TaskGroup updateTaskGroup(Long groupId, String newTitle, String newColor);

}
