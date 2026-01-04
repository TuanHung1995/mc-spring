package com.mc.application.model.item;

import lombok.Data;

import java.util.Date;

@Data
public class CreateItemResponse {
    
    private Long id;
    private String name;
    private Long groupId;
    private Long boardId;
    private double position;
    private Date createdAt;
    
}
