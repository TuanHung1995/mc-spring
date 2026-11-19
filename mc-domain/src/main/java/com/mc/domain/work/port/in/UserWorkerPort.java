package com.mc.domain.work.port.in;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;

public interface UserWorkerPort {

    void processUserRegistration(RegisterIntegrationEvent event);

}
