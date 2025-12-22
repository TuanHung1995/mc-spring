package com.mc.infrastructure.realtime;

import com.mc.domain.event.BoardChangedEvent;
import com.mc.domain.port.RealTimeUpdatePort;
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
}