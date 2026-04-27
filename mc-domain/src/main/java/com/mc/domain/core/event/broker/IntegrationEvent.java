package com.mc.domain.core.event.broker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class IntegrationEvent implements Serializable {

    private final UUID eventId;
    private final LocalDateTime occurredOn;

    protected IntegrationEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

}
