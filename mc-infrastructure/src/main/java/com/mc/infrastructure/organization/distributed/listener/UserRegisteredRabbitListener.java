package com.mc.infrastructure.organization.distributed.listener;

import com.mc.domain.core.event.broker.rabbitMQ.RegisterIntegrationEvent;

import com.mc.domain.organization.port.in.TeamWorkerPort;
import com.mc.infrastructure.iam.config.brokerMQ.RabbitMQIAMConfig;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredRabbitListener {

    private final TeamWorkerPort teamWorkerPort;

    @RabbitListener(queues = RabbitMQIAMConfig.REGISTER_USER_QUEUE, ackMode = "MANUAL")
    public void receiveUserRegisteredEvent(
            RegisterIntegrationEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Received user registered event: {}", event);
        teamWorkerPort.processUserRegistration(event);
    }
}
