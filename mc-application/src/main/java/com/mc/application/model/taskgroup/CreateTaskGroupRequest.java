package com.mc.application.model.taskgroup;

import lombok.Data;

@Data
public class CreateTaskGroupRequest {
    
    private Long boardId;
    private String title;
    private String color;
    
}
