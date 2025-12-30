package com.mc.application.model.board;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrashBoardRequest {

    @NotNull(message = "Board ID cannot be null")
    private Long boardId;

}
