package com.mc.application.model.board;

import lombok.Data;

@Data
public class CreateBoardRequest {

    private Long currentWorkspaceId;
    private String name;
    private String purpose;
    private String privacy;

}
