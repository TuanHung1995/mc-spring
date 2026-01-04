package com.mc.application.model.taskgroup;

import lombok.Data;

@Data
public class UpdateTaskGroupRequest {
    
    private Long groupId;
    private String title;
    private String color;
    private Boolean isCollapsed;
    
}
