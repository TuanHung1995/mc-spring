package com.mc.domain.core.event.broker.rabbitMQ;

import com.mc.domain.core.event.broker.IntegrationEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RegisterIntegrationEvent extends IntegrationEvent {

    private final UUID userId;

    public RegisterIntegrationEvent() {
        super();
        this.userId = null;
    }

    public RegisterIntegrationEvent(UUID userId) {
        super();
        this.userId = userId;
    }
}
