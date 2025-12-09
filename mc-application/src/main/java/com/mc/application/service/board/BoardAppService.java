package com.mc.application.service.board;

import com.mc.application.model.board.ReorderRequest;
import com.mc.domain.model.entity.Board;

import java.util.List;

public interface BoardAppService {

    List<Board> getBoardsForUser();

    void reorderGroup(ReorderRequest request);

    void reorderColumn(ReorderRequest request);

    void reorderItem(ReorderRequest request);

}
