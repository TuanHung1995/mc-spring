package com.mc.application.work.service.impl;

import com.mc.application.work.dto.request.CreateColumnRequest;
import com.mc.application.work.dto.response.ColumnResponse;
import com.mc.application.work.service.BoardColumnAppService;
import com.mc.domain.core.event.BoardChangedEvent;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.work.port.WorkUserContextPort;
import com.mc.domain.work.model.entity.BoardColumn;
import com.mc.domain.work.model.enums.BoardColumnType;
import com.mc.domain.work.repository.BoardColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BoardColumnAppServiceImpl — Application Service (Work Context)
 */
@Service
@RequiredArgsConstructor
@Component("workBoardColumnAppService")
public class BoardColumnAppServiceImpl implements BoardColumnAppService {

    @Qualifier("workBoardColumnRepository")
    private final BoardColumnRepository columnRepository;
    private final WorkUserContextPort workUserContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ColumnResponse createColumn(CreateColumnRequest request) {
        UUID userId = workUserContextPort.getCurrentUser().id();

        Double maxPos = columnRepository.getMaxPositionByBoardId(request.getBoardId());
        double newPos = (maxPos != null) ? maxPos + 65536 : 65536;

        BoardColumnType type = parseType(request.getType());
        BoardColumn col = BoardColumn.create(request.getBoardId(), request.getTitle(), type, newPos, userId);
        BoardColumn saved = columnRepository.save(col);

        publishEvent("COLUMN_CREATED", saved.getId(), saved.getBoardId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ColumnResponse getColumnById(Long columnId) {
        return toResponse(requireColumn(columnId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColumnResponse> getColumnsByBoard(Long boardId) {
        return columnRepository.findAllByBoardId(boardId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ColumnResponse updateColumnTitle(Long columnId, String newTitle) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        BoardColumn col = requireColumn(columnId);
        col.rename(newTitle, userId);
        BoardColumn saved = columnRepository.save(col);
        publishEvent("COLUMN_UPDATED", saved.getId(), saved.getBoardId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteColumn(Long columnId) {
        UUID userId = workUserContextPort.getCurrentUser().id();
        BoardColumn col = requireColumn(columnId);
        Long boardId = col.getBoardId();
        col.trash(userId);
        columnRepository.delete(col);
        publishEvent("COLUMN_DELETED", columnId, boardId);
    }

    // =================================================================
    // HELPERS
    // =================================================================

    private BoardColumn requireColumn(Long columnId) {
        return columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("BoardColumn", "id", columnId));
    }

    private void publishEvent(String type, Long entityId, Long boardId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        payload.put("entityId", entityId);
        payload.put("boardId", boardId);
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    private BoardColumnType parseType(String type) {
        try {
            return type != null ? BoardColumnType.valueOf(type.toUpperCase()) : BoardColumnType.TEXT;
        } catch (IllegalArgumentException e) {
            return BoardColumnType.TEXT;
        }
    }

    private ColumnResponse toResponse(BoardColumn c) {
        return new ColumnResponse(c.getId(), c.getBoardId(), c.getTitle(),
                c.getType() != null ? c.getType().name() : null,
                c.getDescription(), c.getPosition(), c.getWidth(), c.isHidden(), c.getCreatedAt());
    }
}
