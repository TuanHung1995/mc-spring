package com.mc.application.model.taskgroup;

import lombok.Data;

@Data
public class GetTaskGroupResponse {
    
    private Long id;
    private String title;
    private String color;
    private double position;
    private boolean isCollapsed;
    private Long boardId;
    
}
