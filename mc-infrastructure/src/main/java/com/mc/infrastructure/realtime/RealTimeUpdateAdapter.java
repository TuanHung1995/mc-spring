package com.mc.infrastructure.realtime;

import com.mc.domain.port.RealTimeUpdatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealTimeUpdateAdapter implements RealTimeUpdatePort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendBoardUpdate(Long boardId, Object payload) {
        String destination = "/topic/board/" + boardId;
        log.info("Sending real-time update to: {}", destination);

        // Gửi payload vào kênh subscription của Board đó
        messagingTemplate.convertAndSend(destination, payload);
    }

}
