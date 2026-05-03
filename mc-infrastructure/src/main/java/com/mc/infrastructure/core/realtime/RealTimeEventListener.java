package com.mc.infrastructure.core.realtime;

import com.mc.domain.core.event.BoardChangedEvent;
import com.mc.domain.core.port.in.RealTimeUpdatePort;
import com.mc.domain.organization.event.WorkspaceChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Infrastructure listener that bridges Domain Events to WebSocket updates.
 * Listens for {@link BoardChangedEvent} and pushes updates to clients via STOMP.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RealTimeEventListener {

    private final RealTimeUpdatePort realTimeUpdatePort;

    /**
     * Handles the BoardChangedEvent specifically after the database transaction commits.
     * This prevents "race conditions" where the frontend receives a socket message
     * before the data is actually persisted in the database.
     *
     * @param event the domain event containing board ID and change payload
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBoardChangedEvent(BoardChangedEvent event) {
        log.debug("DB Committed. Broadcasting event for Board ID: {}", event.getBoardId());

        // Call Port to send real-time update via WebSocket
        realTimeUpdatePort.sendBoardUpdate(event.getBoardId(), event.getPayload());
    }

    /**
     * Listens for WorkspaceChatEvent specifically AFTER the DB transaction commits.
     * <p>
     * ARCHITECTURE NOTE: Using TransactionPhase.AFTER_COMMIT guarantees that
     * the frontend clients will only receive the WebSocket message AFTER the chat
     * data is safely persisted in the database. This eliminates race conditions
     * where a client fetches chat history before the new message is fully committed.
     * </p>
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWorkspaceChatEvent(WorkspaceChatEvent event) {
        log.debug("DB Committed. Broadcasting chat event for Workspace ID: {}", event.getWorkspaceId());

        // Delegate the actual socket transmission to the Port implementation
        realTimeUpdatePort.sendWorkspaceUpdate(event.getWorkspaceId(), event.getPayload());
    }
}