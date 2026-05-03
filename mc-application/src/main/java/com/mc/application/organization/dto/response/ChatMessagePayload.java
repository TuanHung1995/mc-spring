package com.mc.application.organization.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO representing the payload of a workspace chat message.
 * <p>
 * ARCHITECTURE NOTE: This is a pure Java Record/POJO used to transfer data.
 * It contains no framework-specific annotations and is safe to be serialized
 * into JSON by the Infrastructure layer before sending to the WebSocket broker.
 * </p>
 */
@Getter
@Builder
public class ChatMessagePayload {
    private final Long messageId;
    private final UUID workspaceId;
    private final UUID senderId;
    private final String senderName;
    private final String content;
    private final LocalDateTime timestamp;
}
