package com.mc.domain.service;

import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.User;
import com.mc.domain.model.entity.Workspace;

import java.util.List;

public interface BoardDomainService {

    List<Board> getBoardsForUser(Long userId);

    Board createBoard(Long userId, Long workspaceId, String name, String purpose, String type);

    void deleteBoardPermanently(Long userId, Long boardId);

    void trashBoard(Long currentUserId, Long boardId);
}
