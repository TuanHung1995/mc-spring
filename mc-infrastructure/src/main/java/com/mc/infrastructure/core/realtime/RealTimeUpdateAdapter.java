package com.mc.infrastructure.core.realtime;

import com.mc.domain.core.port.in.RealTimeUpdatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealTimeUpdateAdapter implements RealTimeUpdatePort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendBoardUpdate(UUID boardId, Object payload) {
        String destination = "/topic/board/" + boardId;
        log.info("Sending real-time update to: {}", destination);

        // Gửi payload vào kênh subscription của Board đó
        messagingTemplate.convertAndSend(destination, payload);
    }

    /**
     * Implementation of the Port to route workspace updates to the STOMP broker.
     * <p>
     * ARCHITECTURE NOTE: This is the ONLY class that interacts with Spring's
     * SimpMessagingTemplate. It translates the Domain's request to communicate
     * into the specific protocol (WebSocket/STOMP) required by the infrastructure.
     * </p>
     */
    @Override
    public void sendWorkspaceUpdate(UUID workspaceId, Object payload) {
        // Define the STOMP topic for workspace chat
        String destination = "/topic/workspace/" + workspaceId + "/chat";
        log.info("Sending real-time chat update to: {}", destination);

        // Serialize and push the payload to the broker
        messagingTemplate.convertAndSend(destination, payload);
    }

}
