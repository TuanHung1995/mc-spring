package com.mc.domain.service;

import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.User;
import com.mc.domain.model.entity.Workspace;

import java.util.List;

public interface BoardDomainService {

    List<Board> getBoardsForUser(Long userId);

    Board createBoard(User user, Workspace workspace, String name, String purpose, String type);
}
