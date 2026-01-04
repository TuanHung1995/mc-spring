package com.mc.application.model.item;

import lombok.Data;

import java.util.Date;

@Data
public class GetItemResponse {
    
    private Long id;
    private String name;
    private double position;
    private Long groupId;
    private Long boardId;
    private Date createdAt;
    
}
