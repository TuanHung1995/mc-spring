package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Board;
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

}
