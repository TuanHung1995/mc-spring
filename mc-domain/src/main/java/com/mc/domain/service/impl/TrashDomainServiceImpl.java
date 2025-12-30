package com.mc.domain.service.impl;

import com.mc.domain.model.dto.TrashItem;
import com.mc.domain.model.entity.Board;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.service.TrashDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrashDomainServiceImpl implements TrashDomainService {

    private final BoardRepository boardRepository;

    @Override
    public List<TrashItem> getAllTrashItems(Long userId) {

        // 1. Get Deleted Boards
        List<Board> deletedBoards = boardRepository.findAllDeletedBoards(userId);

        // Future: Add other entities like Workspaces, Items here

        return deletedBoards.stream().map(this::mapBoardToTrashItem).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void restoreBoard(Long boardId) {
        boardRepository.restoreBoard(boardId);
    }
    
    // Helper mapper
    private TrashItem mapBoardToTrashItem(Board board) {
        return TrashItem.builder()
                .id(board.getId())
                .name(board.getName())
                .type("BOARD")
                .deletedAt(board.getDeletedAt())
                .deletedBy(board.getDeletedBy() != null ? board.getDeletedBy().getId() : null)
                .deletedByName(board.getDeletedBy() != null ? board.getDeletedBy().getFullName() : "Unknown")
                .build();
    }
}
