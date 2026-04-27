package com.mc.application.work.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ReorderColumnRequest {

    private UUID targetId;
    private UUID previousId;
    private UUID nextId;

}
