package com.mc.infrastructure.iam.distributed;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;
import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.iam.port.out.AuthMessagePort;
import com.mc.infrastructure.iam.config.brokerMQ.RabbitMQIAMConfig;
import com.mc.infrastructure.organization.config.brokerMQ.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRabbitMQAdapter implements AuthMessagePort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishRegisterEvent(RegisterIntegrationEvent event) {
        log.info("Publishing RegisterIntegrationEvent for userId: {}", event.getUserId());
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQIAMConfig.EXCHANGE_NAME,
                    RabbitMQIAMConfig.REGISTER_USER_ROUTING_KEY,
                    event
            );
            log.info("Successfully published RegisterIntegrationEvent for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to publish RegisterIntegrationEvent: {}", e.getMessage(), e);
            // Depending on reliability requirements, we could rethrow, push to an outbox table, etc.
            // For Eventual Consistency via message broker, we usually let it bubble up to fail the transaction,
            // or use transactional outbox pattern.
            throw new RuntimeException("Failed to publish event to RabbitMQ", e);
        }
    }

}
