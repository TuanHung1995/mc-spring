package com.mc.infrastructure.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket and STOMP messaging.
 * Configures the message broker and registers security interceptors.
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SubscriptionSecurityInterceptor subscriptionSecurityInterceptor;

    /**
     * Registers the STOMP endpoints that clients will connect to.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Configure appropriate CORS for production
                .withSockJS();
    }

    /**
     * Configures the message broker options.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configures the client inbound channel.
     * Registers the {@link SubscriptionSecurityInterceptor} to validate messages
     * (specifically SUBSCRIBE) coming from clients.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Register the security interceptor at the beginning of the chain
        registration.interceptors(subscriptionSecurityInterceptor);
    }
}
