package com.mc.domain.service;

import com.mc.domain.model.entity.Board;

import java.util.List;

public interface BoardDomainService {

    List<Board> getBoardsForUser(Long userId);

}
