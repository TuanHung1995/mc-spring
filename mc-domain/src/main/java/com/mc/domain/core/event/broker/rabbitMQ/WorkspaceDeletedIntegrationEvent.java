package com.mc.domain.core.event.broker.rabbitMQ;

import com.mc.domain.core.event.broker.IntegrationEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class WorkspaceDeletedIntegrationEvent extends IntegrationEvent {
    private final UUID workspaceId;
    private final UUID deletedByUserId;

    public WorkspaceDeletedIntegrationEvent() {
        super();
        this.workspaceId = null;
        this.deletedByUserId = null;
    }

    public WorkspaceDeletedIntegrationEvent(UUID workspaceId, UUID deletedByUserId) {
        super();
        this.workspaceId = workspaceId;
        this.deletedByUserId = deletedByUserId;
    }
}
