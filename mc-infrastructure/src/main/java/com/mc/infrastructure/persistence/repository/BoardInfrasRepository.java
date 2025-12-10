package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Board;
import com.mc.domain.repository.BoardRepository;
import com.mc.infrastructure.persistence.mapper.BoardJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardInfrasRepository implements BoardRepository {

    private final BoardJPAMapper boardJPAMapper;

    @Override
    public Optional<Board> findById(Long boardId) {
        return boardJPAMapper.findById(boardId);
    }

    @Override
    public List<Board> findAllByUserId(Long userId) {
        return boardJPAMapper.findAllByUserId(userId);
    }

    @Override
    public Board save(Board board) {
        return boardJPAMapper.save(board);
    }

}
