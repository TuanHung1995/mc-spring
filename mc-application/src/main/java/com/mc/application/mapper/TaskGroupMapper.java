package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.domain.model.entity.TaskGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskGroupMapper {

    @Mapping(source = "title", target = "name")
    UpdateBoardResponse toUpdateBoardResponse(TaskGroup taskGroup);

}
