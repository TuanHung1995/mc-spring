package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.domain.model.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    UpdateBoardResponse toUpdateBoardResponse(Item item);

}
