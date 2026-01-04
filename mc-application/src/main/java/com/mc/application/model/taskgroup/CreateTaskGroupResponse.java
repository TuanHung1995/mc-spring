package com.mc.application.model.taskgroup;

import lombok.Data;

@Data
public class CreateTaskGroupResponse {
    
    private Long id;
    private String title;
    private String color;
    private Long boardId;
    private double position;
    
}
