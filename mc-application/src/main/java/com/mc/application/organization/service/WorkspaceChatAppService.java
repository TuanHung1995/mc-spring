package com.mc.application.organization.service;

import java.util.UUID;

public interface WorkspaceChatAppService {

        /**
        * Publishes a chat message to all clients subscribed to the workspace's chat topic.
        * <p>
        * ARCHITECTURE NOTE: This method is part of the Application layer. It orchestrates
        * the process of sending a chat message. It may involve validating the message,
        * transforming it into a DTO, and then using the RealTimeUpdatePort to dispatch
        * it to the WebSocket broker. The Application layer remains agnostic of how the
        * message is delivered (WebSockets, Push Notifications, etc.).
        * </p>
        *
        * @param workspaceId The unique identifier of the workspace.
        * @param senderId The unique identifier of the user sending the message.
        * @param content The content of the chat message.
        */
        void sendMessage(UUID workspaceId, UUID senderId, String content);

}
