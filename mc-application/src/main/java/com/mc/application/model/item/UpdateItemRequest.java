package com.mc.application.model.item;

import lombok.Data;

@Data
public class UpdateItemRequest {
    
    private Long itemId;
    private String name;
    
}
