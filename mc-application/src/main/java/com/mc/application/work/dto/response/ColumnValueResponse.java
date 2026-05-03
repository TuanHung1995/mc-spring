package com.mc.application.work.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnValueResponse {

    private UUID id;
    private UUID boardId;
    private Long columnId;
    private UUID taskGroupId;
    private UUID itemId;
    private String value;
    private String textValue;
    private String color;
    private String type;
    private UUID createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
