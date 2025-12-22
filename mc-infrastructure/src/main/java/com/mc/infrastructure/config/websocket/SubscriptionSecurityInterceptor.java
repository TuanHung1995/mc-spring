package com.mc.infrastructure.config.websocket;

import com.mc.domain.repository.BoardMemberRepository;
import com.mc.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interceptor to enforce security on WebSocket subscriptions.
 * It validates if the authenticated user has permission to subscribe to a specific topic.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionSecurityInterceptor implements ChannelInterceptor {

    private final BoardMemberRepository boardMemberRepository;

    // Regex to extract boardId from path: /topic/board/123
    private static final Pattern BOARD_TOPIC_PATTERN = Pattern.compile(".*/topic/board/(\\d+)$");

    /**
     * Intercepts the message before it is sent to the channel.
     * Validates SUBSCRIBE commands to ensure the user is a member of the requested board.
     *
     * @param message the incoming message
     * @param channel the channel the message is sent to
     * @return the message if allowed
     * @throws IllegalArgumentException or AccessDeniedException if validation fails
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            validateSubscription(accessor.getUser(), destination);
        }

        return message;
    }

    private void validateSubscription(Principal principal, String destination) {
        if (destination == null || principal == null) {
            return; // Let standard security handle unauthenticated users
        }

        Matcher matcher = BOARD_TOPIC_PATTERN.matcher(destination);
        if (matcher.matches()) {
            Long boardId = Long.parseLong(matcher.group(1));
            Long userId = getUserIdFromPrincipal(principal);

            log.debug("Validating subscription: User {} -> Board {}", userId, boardId);

            boolean isMember = boardMemberRepository.existsByBoardIdAndUserId(boardId, userId);

            if (!isMember) {
                log.warn("Unauthorized subscription attempt. User {} tried to subscribe to Board {}", userId, boardId);
                throw new IllegalArgumentException("Access Denied: You are not a member of this board.");
            }
        }
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken token
                && token.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new IllegalArgumentException("Invalid Principal type");
    }
}