package com.mc.domain.work.port.in;

import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;

/**
 * WorkspaceWorkerPort — Inbound Port (Work Context)
 *
 * <p>Handles background integration events asynchronously. In this case,
 * when a Workspace is deleted in the Organization bounded context, this
 * port performs cascade soft-deletions on all associated Boards.</p>
 */
public interface WorkspaceWorkerPort {

    /**
     * Processes a workspace deletion by soft-deleting all boards
     * belonging to that workspace.
     *
     * @param event The integration event from the broker.
     */
    void processWorkspaceDeletion(WorkspaceDeletedIntegrationEvent event);
}
