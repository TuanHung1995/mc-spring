package com.mc.infrastructure.organization.distributed.listener;

import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.organization.port.out.WorkspaceMessagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WorkspaceEventDispatcher {

    private final WorkspaceMessagePort workspaceMessagePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWorkspaceDeletedEvent(WorkspaceDeletedIntegrationEvent event) {
        workspaceMessagePort.publishWorkspaceDeletedEvent(event);
    }
}
