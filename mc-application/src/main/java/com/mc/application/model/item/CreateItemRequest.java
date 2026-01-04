package com.mc.application.model.item;

import lombok.Data;

@Data
public class CreateItemRequest {
    
    private Long boardId;
    private Long groupId;
    private String name;
    
}
