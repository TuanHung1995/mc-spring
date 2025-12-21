package com.mc.domain.repository;

import com.mc.domain.model.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findById(Long boardId);

    List<Board> findAllByUserId(Long userId);

    Board save(Board board);

}
