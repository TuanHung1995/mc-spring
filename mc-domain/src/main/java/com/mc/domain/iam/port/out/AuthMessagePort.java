package com.mc.domain.iam.port.out;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;

/**
 * AuthMessagePort — Outbound Port (IAM Context)
 *
 * <p>Used to send authentication-related messages (e.g., email notifications)
 * out to external systems. The implementation lives in the infrastructure layer
 * and adapts to a specific technology (e.g., message broker, email service).</p>
 */
public interface AuthMessagePort {

    /**
     * Publishes an integration event when a new user registers.
     *
     * @param event The event payload.
     */
    void publishRegisterEvent(RegisterIntegrationEvent event);

}
