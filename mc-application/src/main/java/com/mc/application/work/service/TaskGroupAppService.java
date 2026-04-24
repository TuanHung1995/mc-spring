package com.mc.application.work.service;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TaskGroupAppService — Application Service Port (Work Context)
 */
public interface TaskGroupAppService {

    TaskGroupResponse createGroup(CreateTaskGroupRequest request);
    TaskGroupResponse getGroupById(Long groupId);
    List<TaskGroupResponse> getGroupsByBoard(Long boardId);
    TaskGroupResponse updateGroup(Long groupId, UpdateTaskGroupRequest request);

    void deleteGroup(Long groupId);
    void archiveGroup(Long groupId);
    void unarchiveGroup(Long groupId);
    void restoreGroup(Long groupId);
    void permanentDeleteGroup(Long groupId);

    List<TaskGroupResponse> getArchivedGroupsByBoard(Long boardId);
    List<TaskGroupResponse> getTrashedGroupsByBoard(Long boardId);
}
