package com.mc.application.service.taskgroup;

import com.mc.application.model.taskgroup.*;

import java.util.List;

public interface TaskGroupAppService {

    CreateTaskGroupResponse createGroup(CreateTaskGroupRequest request);

    GetTaskGroupResponse getGroup(Long groupId);

    List<GetTaskGroupResponse> getGroupsByBoard(Long boardId);

    UpdateTaskGroupResponse updateGroup(UpdateTaskGroupRequest request);

    void deleteGroup(DeleteTaskGroupRequest request);

}
