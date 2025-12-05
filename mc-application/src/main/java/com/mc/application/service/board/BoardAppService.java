package com.mc.application.service.board;

import com.mc.domain.model.entity.Board;

import java.util.List;

public interface BoardAppService {

    List<Board> getBoardsForUser();

}
