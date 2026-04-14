package com.mc.application.core.model.board;

import lombok.Data;

@Data
public class InviteRequest {
    private String email;
    private String role;
}
