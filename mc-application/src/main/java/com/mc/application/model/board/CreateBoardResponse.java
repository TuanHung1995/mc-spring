package com.mc.application.model.board;

import lombok.Data;

@Data
public class CreateBoardResponse {

    private Long id;
    private String name;
    private String type;

}
