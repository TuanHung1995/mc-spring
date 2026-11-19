package com.mc.infrastructure.iam.config.brokerMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQIAMConfig {

    public static final String EXCHANGE_NAME = "iam.exchange";
    public static final String REGISTER_USER_QUEUE = "iam.queue.register_user";
    public static final String REGISTER_USER_ROUTING_KEY = "iam.routing.register_user";

    // DLQ Constants
    public static final String DLX_EXCHANGE_NAME = "iam.exchange.dlx";
    public static final String REGISTER_USER_DLQ = "iam.queue.register_user.dlq";
    public static final String REGISTER_USER_DLQ_ROUTING_KEY = "iam.routing.register_user.dlq";

    // 1. Tạo Topic Exchange (linh hoạt, dùng chung cho nhiều module)
    @Bean
    public TopicExchange iamExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Tạo Queue với cấu hình DLQ
    @Bean
    public Queue registerUserQueue() {
        return QueueBuilder.durable(REGISTER_USER_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", REGISTER_USER_DLQ_ROUTING_KEY)
                .build();
    }

    // 3. Binding Queue với Exchange bằng Routing Key
    @Bean
    public Binding bindingRegisterUser(Queue registerUserQueue, TopicExchange iamExchange) {
        return BindingBuilder.bind(registerUserQueue)
                .to(iamExchange)
                .with(REGISTER_USER_ROUTING_KEY);
    }

    // 4. Tạo DLQ Exchange
    @Bean
    public TopicExchange dlqIAMExchange() {
        return new TopicExchange(DLX_EXCHANGE_NAME);
    }

    // 5. Tạo Dead Letter Queue
    @Bean
    public Queue registerUserDlq() {
        return QueueBuilder.durable(REGISTER_USER_DLQ).build();
    }

    // 6. Binding DLQ với DLX
    @Bean
    public Binding bindingRegisterUserDlq(Queue registerUserDlq, TopicExchange dlqIAMExchange) {
        return BindingBuilder.bind(registerUserDlq)
                .to(dlqIAMExchange)
                .with(REGISTER_USER_DLQ_ROUTING_KEY);
    }

}
