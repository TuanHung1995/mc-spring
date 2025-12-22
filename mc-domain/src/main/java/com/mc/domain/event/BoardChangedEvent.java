package com.mc.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Represents a domain event indicating that the state of a board has changed.
 * <p>
 * This is a POJO (Plain Old Java Object) residing in the Domain layer,
 * allowing both Application (Publisher) and Infrastructure (Listener) to access it
 * without violating dependency rules.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class BoardChangedEvent {

    /**
     * The unique identifier of the board that has changed.
     */
    private final Long boardId;

    /**
     * The specific details of the change (e.g., REORDER, UPDATE_ITEM).
     * This map serves as the payload for the real-time notification.
     */
    private final Map<String, Object> payload;
}
