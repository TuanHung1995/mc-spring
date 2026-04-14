package com.mc.application.core.service.invite;

import com.mc.application.core.model.board.InviteRequest;

public interface InviteAppService {

    void inviteMember(Long boardId, InviteRequest request);
    void acceptInvitation(String token);

}
