package com.mc.application.model.column;

import com.mc.domain.model.enums.BoardColumnType;
import lombok.Data;

@Data
public class CreateColumnResponse {
    
    private Long id;
    private String title;
    private BoardColumnType type;
    private Long boardId;
    private double position;
    
}
