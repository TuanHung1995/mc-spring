package com.mc.application.work.service;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * TaskGroupAppService — Application Service Port (Work Context)
 */
public interface TaskGroupAppService {

    TaskGroupResponse createGroup(CreateTaskGroupRequest request);
    TaskGroupResponse getGroupById(UUID groupId);
    List<TaskGroupResponse> getGroupsByBoard(UUID boardId);
    TaskGroupResponse updateGroup(UUID groupId, UpdateTaskGroupRequest request);

    void deleteGroup(UUID groupId);
    void archiveGroup(UUID groupId);
    void unarchiveGroup(UUID groupId);
    void restoreGroup(UUID groupId);
    void permanentDeleteGroup(UUID groupId);

    List<TaskGroupResponse> getArchivedGroupsByBoard(UUID boardId);
    List<TaskGroupResponse> getTrashedGroupsByBoard(UUID boardId);
}
