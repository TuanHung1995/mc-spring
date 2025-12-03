package com.mc.application.model.board;

import lombok.Data;

@Data
public class InviteRequest {
    private String email;
    private String role;
}
