package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.application.model.taskgroup.*;
import com.mc.domain.model.entity.TaskGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskGroupMapper {

    @Mapping(target = "name", source = "title")
    UpdateBoardResponse toUpdateBoardResponse(TaskGroup group);

    @Mapping(target = "boardId", source = "board.id")
    CreateTaskGroupResponse toCreateTaskGroupResponse(TaskGroup group);

    @Mapping(target = "boardId", source = "board.id")
    GetTaskGroupResponse toGetTaskGroupResponse(TaskGroup group);

    UpdateTaskGroupResponse toUpdateTaskGroupResponse(TaskGroup group);

}
