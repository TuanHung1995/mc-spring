package com.mc.application.model.item;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateItemResponse {
    
    private Long id;
    private String name;
    private Date updatedAt;
    
}
