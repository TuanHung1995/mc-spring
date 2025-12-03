package com.mc.application.service.invite;

import com.mc.application.model.board.InviteRequest;

public interface InviteAppService {

    void inviteMember(Long boardId, InviteRequest request);
    void acceptInvitation(String token);

}
