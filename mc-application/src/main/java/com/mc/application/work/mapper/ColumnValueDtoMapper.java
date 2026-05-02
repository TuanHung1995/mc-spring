package com.mc.application.work.mapper;

import com.mc.application.work.dto.response.ColumnValueResponse;
import com.mc.domain.work.model.entity.ColumnValue;
import org.springframework.stereotype.Component;

@Component
public class ColumnValueDtoMapper {

    public ColumnValueResponse toResponse(ColumnValue columnValue) {
        if (columnValue == null) return null;

        return ColumnValueResponse.builder()
                .id(columnValue.getId())
                .boardId(columnValue.getBoardId())
                .columnId(columnValue.getColumnId())
                .taskGroupId(columnValue.getTaskGroupId())
                .itemId(columnValue.getItemId())
                .value(columnValue.getValue())
                .textValue(columnValue.getTextValue())
                .color(columnValue.getColor())
                .type(columnValue.getType())
                .createdAt(columnValue.getCreatedAt())
                .updatedAt(columnValue.getUpdatedAt())
                .build();
    }

}
