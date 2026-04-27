package com.mc.domain.organization.port.out;

import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;

/**
 * WorkspaceMessagePort — Outbound Port (Organization Context)
 *
 * <p>Used to publish integration events related to Workspace changes
 * out to the message broker. The implementation lives in the infrastructure layer
 * and adapts to a specific technology (e.g., RabbitMQ).</p>
 */
public interface WorkspaceMessagePort {

    /**
     * Publishes an integration event when a Workspace is soft-deleted.
     *
     * @param event The event payload.
     */
    void publishWorkspaceDeletedEvent(WorkspaceDeletedIntegrationEvent event);
}
