package com.mc.application.mapper;

import com.mc.application.model.board.UpdateBoardResponse;
import com.mc.application.model.item.*;
import com.mc.domain.model.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    UpdateBoardResponse toUpdateBoardResponse(Item item);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "boardId", source = "board.id")
    CreateItemResponse toCreateItemResponse(Item item);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "boardId", source = "board.id")
    GetItemResponse toGetItemResponse(Item item);

    UpdateItemResponse toUpdateItemResponse(Item item);

}
