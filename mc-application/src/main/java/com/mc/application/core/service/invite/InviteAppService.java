package com.mc.application.core.service.invite;

import com.mc.application.core.model.board.InviteRequest;

import java.util.UUID;

public interface InviteAppService {

    void inviteMember(UUID boardId, InviteRequest request);
    void acceptInvitation(String token);

}
