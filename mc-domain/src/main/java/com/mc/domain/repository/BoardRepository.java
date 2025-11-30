package com.mc.domain.repository;

import com.mc.domain.model.entity.Board;

import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findById(Long boardId);

}
