package com.mc.application.service.taskgroup;

import com.mc.application.model.taskgroup.*;

import java.util.List;

public interface TaskGroupAppService {

    CreateTaskGroupResponse createGroup(CreateTaskGroupRequest request);

    GetTaskGroupResponse getGroup(Long groupId);

    List<GetTaskGroupResponse> getGroupsByBoard(Long boardId);

    UpdateTaskGroupResponse updateGroup(UpdateTaskGroupRequest request);

    void deleteGroup(DeleteTaskGroupRequest request);

    void archiveGroup(ArchiveTaskGroupRequest request);

    void unarchiveGroup(UnarchiveTaskGroupRequest request);

    List<GetTaskGroupResponse> getArchivedGroupsByBoard(Long boardId);

    void restoreGroup(RestoreTaskGroupRequest request);

    void permanentDeleteGroup(PermanentDeleteTaskGroupRequest request);

    List<GetTaskGroupResponse> getTrashedGroupsByBoard(Long boardId);

}
