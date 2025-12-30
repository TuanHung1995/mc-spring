package com.mc.application.model.board;

import lombok.Data;

@Data
public class UpdateBoardRequest {

    private Long targetId;
    private String type;
    private String value;
    private String textValue;
    private String color;

}
