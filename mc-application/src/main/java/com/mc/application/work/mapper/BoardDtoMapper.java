package com.mc.application.work.mapper;

import com.mc.application.work.dto.response.BoardResponse;
import com.mc.domain.work.model.entity.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardDtoMapper {

    public BoardResponse toResponse(Board board) {
        if (board == null) return null;

        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .type(board.getType().toString())
                .purpose(board.getPurpose())
                .workspaceId(board.getWorkspaceId())
                .teamId(board.getTeamId())
                .createdById(board.getCreatedById())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
