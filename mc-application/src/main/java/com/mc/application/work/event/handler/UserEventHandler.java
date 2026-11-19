package com.mc.application.work.event.handler;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;
import com.mc.domain.work.port.in.UserWorkerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventHandler implements UserWorkerPort {

    @Override
    public void processUserRegistration(RegisterIntegrationEvent event) {
        log.info("Processing asynchronous create board for userId: {}", event.getUserId());
        // Implement the logic to handle user registration event
        // For example, you might want to create a new user in your system based on the event data
    }

}
