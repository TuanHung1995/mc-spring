package com.mc.application.work.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ReorderColumnRequest {

    private Long targetId;
    private Long previousId;
    private Long nextId;

}
