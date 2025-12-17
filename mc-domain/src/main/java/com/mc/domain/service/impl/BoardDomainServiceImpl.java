package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.User;
import com.mc.domain.model.entity.Workspace;
import com.mc.domain.model.enums.BoardType;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.service.BoardDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardDomainServiceImpl implements BoardDomainService {

    private final BoardRepository boardRepository;

    @Override
    public List<Board> getBoardsForUser(Long userId) {
        return boardRepository.findAllByUserId(userId);
    }

    @Override
    public Board createBoard(User user, Workspace workspace, String name, String purpose, String type) {

        Board newBoard = new Board();
        newBoard.setName(name);
        newBoard.setType(BoardType.valueOf(type));
        newBoard.setCreatedBy(user);
        newBoard.setWorkspace(workspace);

        return boardRepository.save(newBoard);

    }

}
