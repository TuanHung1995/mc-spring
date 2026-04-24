package com.mc.infrastructure.work.distributed.board.listener;

import com.mc.domain.work.port.in.WorkspaceWorkerPort;
import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.infrastructure.organization.config.brokerMQ.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * WorkspaceDeletedRabbitListener — Inbound Adapter (Work Context)
 *
 * <p>Listens to the RabbitMQ queue for workspace deletion events. Upon receiving
 * a message, this adapter delegates the actual business logic back to the Application
 * Service (via {@link WorkspaceWorkerPort}).</p>
 *
 * <p>Includes MANUAL Acknowledgment logic to ensure the Worker Service has successfully
 * processed the batch before removing the message from the queue. If it crashes mid-process,
 * the message is not acked and RabbitMQ will requeue it safely.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceDeletedRabbitListener {

    private final WorkspaceWorkerPort workspaceWorkerPort;

    @RabbitListener(queues = RabbitMQConfig.DELETE_WORKSPACE_QUEUE, ackMode = "MANUAL")
    public void receiveWorkspaceDeletedEvent(
            WorkspaceDeletedIntegrationEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        log.info("Received WorkspaceDeletedIntegrationEvent from RabbitMQ: {}", event);

        try {
            // Delegate logic to application layer
            workspaceWorkerPort.processWorkspaceDeletion(event);

            // Manual Ack (success)
            channel.basicAck(deliveryTag, false);
            log.info("Successfully acknowledged message with delivery tag: {}", deliveryTag);
        } catch (Exception e) {
            log.error("Error processing WorkspaceDeletedIntegrationEvent for workspaceId: {}", event.getWorkspaceId(), e);

            // Nack the message so it goes back to the queue (or DLQ if configured)
            // Multiple is false, requeue is false (so it routes to the Dead Letter Exchange)
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
