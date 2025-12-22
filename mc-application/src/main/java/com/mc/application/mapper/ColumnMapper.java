package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ColumnMapper {

    @Mapping(source = "title", target = "name")
    UpdateBoardResponse toUpdateBoardResponse(Column column);

}
