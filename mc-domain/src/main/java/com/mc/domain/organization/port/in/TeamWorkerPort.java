package com.mc.domain.organization.port.in;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;

public interface TeamWorkerPort {

    void processUserRegistration(RegisterIntegrationEvent event);

}
