package com.mc.domain.organization.service;

import java.util.UUID;

public interface InviteDomainService {

    void sendInvitation(UUID boardId, String email, String role);
    void addUserToBoard(UUID boardId, String email, String roleName);

}
