package com.mc.domain.core.event.broker.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * WorkspaceDeletedIntegrationEvent — Shared Core Integration Event
 *
 * <p>Dispatched over RabbitMQ when a Workspace is soft-deleted.
 * Other bounded contexts (e.g., Work) consume this event to cascade
 * deletions asynchronously (e.g., batch deleting all associated Boards).</p>
 */
public record WorkspaceDeletedIntegrationEvent(
        UUID workspaceId,
        UUID deletedById,
        LocalDateTime deletedAt
) {}
