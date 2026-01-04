package com.mc.application.model.column;

import com.mc.domain.model.enums.BoardColumnType;
import lombok.Data;

@Data
public class CreateColumnRequest {
    
    private Long boardId;
    private String title;
    private BoardColumnType type;
    private String description;
    
}
