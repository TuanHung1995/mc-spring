package com.mc.application.service.board;

import com.mc.application.model.board.CreateBoardRequest;
import com.mc.application.model.board.CreateBoardResponse;
import com.mc.application.model.board.ReorderRequest;
import com.mc.domain.model.entity.Board;

import java.util.List;

public interface BoardAppService {

    List<Board> getBoardsForUser();

    CreateBoardResponse createBoard(CreateBoardRequest board);

    void reorderGroup(ReorderRequest request);

    void reorderColumn(ReorderRequest request);

    void reorderItem(ReorderRequest request);

}
