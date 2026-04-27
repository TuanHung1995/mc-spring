package com.mc.domain.organization.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Domain Event representing a new chat message published in a Workspace.
 * <p>
 * ARCHITECTURE NOTE: This resides in the Domain layer. It acts as an internal
 * notification mechanism. By using Domain Events, we decouple the core business
 * logic from the delivery mechanism (WebSockets, Emails, Push Notifications, etc.).
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class WorkspaceChatEvent {

    /**
     * The ID of the workspace where the chat occurred.
     * Used by the infrastructure layer to route the message to the correct STOMP topic.
     */
    private final UUID workspaceId;

    /**
     * The actual chat payload to be delivered to connected clients.
     */
    private final Object payload;
}
