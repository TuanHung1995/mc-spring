package com.mc.application.core.service.invite.impl;

import com.mc.application.core.model.board.InviteRequest;
import com.mc.application.core.service.invite.InviteAppService;
import com.mc.domain.core.port.in.RealTimeUpdatePort;
import com.mc.domain.organization.service.InviteDomainService;
import com.mc.infrastructure.core.config.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteAppServiceImpl implements InviteAppService {

    private final InviteDomainService inviteDomainService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RealTimeUpdatePort realTimeUpdatePort;

    @Value("${constants.frontend}")
    private String frontendUrl;

    // API 1: Gửi lời mời
    @Override
    public void inviteMember(UUID boardId, InviteRequest request) {
        // 1. Tạo Token
        String token = jwtTokenProvider.generateInviteToken(request.getEmail(), boardId, request.getRole());

        // 2. Tạo Link (Trỏ về trang Frontend xử lý invite)
        String inviteLink = frontendUrl.replace("/oauth2/redirect", "") + "/accept-invite?token=" + token;

        // 3. Gọi Domain để gửi mail
        inviteDomainService.sendInvitation(boardId, request.getEmail(), inviteLink);
    }

    // API 2: Chấp nhận lời mời (Dành cho User đã có tài khoản và đang Login)
    @Override
    public void acceptInvitation(String token) {
        // 1. Validate & Parse Token
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid invitation token");
        }

        Claims claims = jwtTokenProvider.getInviteClaims(token);
        String email = claims.getSubject();
        UUID boardId = claims.get("boardId", UUID.class);
        String role = claims.get("role", String.class);

        // 2. Add vào Board
        inviteDomainService.addUserToBoard(boardId, email, role);

        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("type", "MEMBER_JOINED");
        updatePayload.put("email", email);
        updatePayload.put("role", role);
        updatePayload.put("message", "User " + email + " has joined the board.");

        realTimeUpdatePort.sendBoardUpdate(boardId, updatePayload);
    }
}
