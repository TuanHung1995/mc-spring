package com.mc.infrastructure.organization.distributed;

import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.organization.port.out.WorkspaceMessagePort;
import com.mc.infrastructure.organization.config.brokerMQ.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * WorkspaceRabbitMQAdapter — Outbound Adapter (Infrastructure Layer)
 *
 * <p>Implementation of {@link WorkspaceMessagePort} that serializes events to JSON
 * and publishes them to RabbitMQ. Fits cleanly into the Ports and Adapters architecture,
 * decoupling the Domain/App layers from Spring AMQP.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceRabbitMQAdapter implements WorkspaceMessagePort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishWorkspaceDeletedEvent(WorkspaceDeletedIntegrationEvent event) {
        log.info("Publishing WorkspaceDeletedIntegrationEvent for workspaceId: {}", event.workspaceId());
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.DELETE_WORKSPACE_ROUTING_KEY,
                    event
            );
            log.info("Successfully published WorkspaceDeletedIntegrationEvent for workspaceId: {}", event.workspaceId());
        } catch (Exception e) {
            log.error("Failed to publish WorkspaceDeletedIntegrationEvent: {}", e.getMessage(), e);
            // Depending on reliability requirements, we could rethrow, push to an outbox table, etc.
            // For Eventual Consistency via message broker, we usually let it bubble up to fail the transaction,
            // or use transactional outbox pattern.
            throw new RuntimeException("Failed to publish event to RabbitMQ", e);
        }
    }
}
