package com.mc.application.mapper;

import com.mc.application.model.board.CreateBoardResponse;
import com.mc.domain.model.entity.Board;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    CreateBoardResponse toCreateBoardResponse(Board board);

}
