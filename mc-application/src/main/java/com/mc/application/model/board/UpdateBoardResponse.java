package com.mc.application.model.board;

import lombok.Data;

import java.util.List;

@Data
public class UpdateBoardResponse {

    private Long id;
    private String name;
    private List<ColumnValueDTO> columnValues;

}
