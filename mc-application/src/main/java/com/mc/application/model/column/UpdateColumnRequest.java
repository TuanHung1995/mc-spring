package com.mc.application.model.column;

import lombok.Data;

@Data
public class UpdateColumnRequest {
    
    private Long columnId;
    private String title;
    private String description;
    private Integer width;
    private Boolean isHidden;
    
}
