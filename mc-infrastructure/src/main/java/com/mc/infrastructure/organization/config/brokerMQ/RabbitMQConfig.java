package com.mc.infrastructure.organization.config.brokerMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "work.exchange";
    public static final String DELETE_WORKSPACE_QUEUE = "work.queue.delete_workspace";
    public static final String DELETE_WORKSPACE_ROUTING_KEY = "work.routing.delete_workspace";

    // DLQ Constants
    public static final String DLX_EXCHANGE_NAME = "work.exchange.dlx";
    public static final String DELETE_WORKSPACE_DLQ = "work.queue.delete_workspace.dlq";
    public static final String DELETE_WORKSPACE_DLQ_ROUTING_KEY = "work.routing.delete_workspace.dlq";

    // 1. Tạo Topic Exchange (linh hoạt, dùng chung cho nhiều module)
    @Bean
    public TopicExchange workExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Tạo Queue với cấu hình DLQ
    @Bean
    public Queue deleteWorkspaceQueue() {
        return QueueBuilder.durable(DELETE_WORKSPACE_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DELETE_WORKSPACE_DLQ_ROUTING_KEY)
                .build();
    }

    // 3. Binding Queue với Exchange bằng Routing Key
    @Bean
    public Binding bindingDeleteWorkspace(Queue deleteWorkspaceQueue, TopicExchange workExchange) {
        return BindingBuilder.bind(deleteWorkspaceQueue)
                .to(workExchange)
                .with(DELETE_WORKSPACE_ROUTING_KEY);
    }

    // 4. Tạo DLQ Exchange
    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(DLX_EXCHANGE_NAME);
    }

    // 5. Tạo Dead Letter Queue
    @Bean
    public Queue deleteWorkspaceDlq() {
        return QueueBuilder.durable(DELETE_WORKSPACE_DLQ).build();
    }

    // 6. Binding DLQ với DLX
    @Bean
    public Binding bindingDeleteWorkspaceDlq(Queue deleteWorkspaceDlq, TopicExchange dlqExchange) {
        return BindingBuilder.bind(deleteWorkspaceDlq)
                .to(dlqExchange)
                .with(DELETE_WORKSPACE_DLQ_ROUTING_KEY);
    }
}
