package com.mc.domain.service;

public interface InviteDomainService {

    void sendInvitation(Long boardId, String email, String role);
    void addUserToBoard(Long boardId, String email, String roleName);

}
