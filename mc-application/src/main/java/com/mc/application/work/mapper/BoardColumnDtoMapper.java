package com.mc.application.work.mapper;

import com.mc.application.work.dto.response.ColumnResponse;
import com.mc.application.work.dto.response.ColumnValueResponse;
import com.mc.domain.work.model.entity.BoardColumn;
import com.mc.domain.work.model.entity.ColumnValue;
import org.springframework.stereotype.Component;

@Component
public class BoardColumnDtoMapper {

    public ColumnResponse toResponse(BoardColumn column) {
        if (column == null) return null;

        return ColumnResponse.builder()
                .id(column.getId())
                .boardId(column.getBoardId())
                .title(column.getTitle())
                .type(column.getType().toString())
                .description(column.getDescription())
                .position(column.getPosition())
                .width(column.getWidth())
                .hidden(column.isHidden())
                .createdAt(column.getCreatedAt())
                .build();
    }

}
