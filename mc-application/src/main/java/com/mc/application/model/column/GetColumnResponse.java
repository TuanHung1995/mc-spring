package com.mc.application.model.column;

import com.mc.domain.model.enums.BoardColumnType;
import lombok.Data;

@Data
public class GetColumnResponse {
    
    private Long id;
    private String title;
    private BoardColumnType type;
    private double position;
    private int width;
    private boolean isHidden;
    private Long boardId;
    
}
