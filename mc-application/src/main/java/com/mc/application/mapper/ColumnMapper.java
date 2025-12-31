package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.application.model.column.*;
import com.mc.domain.model.entity.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColumnMapper {

    @Mapping(target = "name", source = "title")
    UpdateBoardResponse toUpdateBoardResponse(Column column);

    @Mapping(target = "boardId", source = "board.id")
    CreateColumnResponse toCreateColumnResponse(Column column);

    @Mapping(target = "boardId", source = "board.id")
    GetColumnResponse toGetColumnResponse(Column column);

    UpdateColumnResponse toUpdateColumnResponse(Column column);

}
