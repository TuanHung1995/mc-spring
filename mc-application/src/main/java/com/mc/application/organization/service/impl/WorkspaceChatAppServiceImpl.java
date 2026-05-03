package com.mc.application.organization.service.impl;

import com.mc.application.organization.dto.response.ChatMessagePayload;
import com.mc.application.organization.service.WorkspaceChatAppService;
import com.mc.domain.organization.event.WorkspaceChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceChatAppServiceImpl implements WorkspaceChatAppService {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Processes an incoming chat message, persists it, and triggers a domain event.
     * <p>
     * ARCHITECTURE NOTE: Notice that there is NO WebSocket logic here.
     * We simply publish a Domain Event. If we decide to swap WebSockets with
     * Server-Sent Events (SSE) or Firebase tomorrow, this class remains 100% unchanged.
     * </p>
     */
    @Override
    @Transactional
    public void sendMessage(UUID workspaceId, UUID senderId, String content) {
        log.info("Processing new chat message for workspace: {}", workspaceId);

        // 1. Validate business rules & Save to Database (Pseudo-code)
        // ChatEntity savedChat = chatRepository.save(new ChatEntity(workspaceId, senderId, content));

        // 2. Construct the Payload
        ChatMessagePayload payload = ChatMessagePayload.builder()
                // .messageId(savedChat.getId())
                .workspaceId(workspaceId)
                .senderId(senderId)
                // .senderName(user.getName())
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        // 3. Publish the Domain Event
        WorkspaceChatEvent event = new WorkspaceChatEvent(workspaceId, payload);
        eventPublisher.publishEvent(event);

        log.debug("Published WorkspaceChatEvent for workspace: {}", workspaceId);
    }

}
