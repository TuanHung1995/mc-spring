package com.mc.application.service.column.impl;

import com.mc.application.mapper.ColumnMapper;
import com.mc.application.model.column.*;
import com.mc.application.service.column.ColumnAppService;
import com.mc.domain.event.BoardChangedEvent;
import com.mc.domain.model.entity.Column;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.ColumnDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnAppServiceImpl implements ColumnAppService {

    private final ColumnDomainService columnDomainService;
    private final ColumnMapper columnMapper;
    private final UserContextPort userContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CreateColumnResponse createColumn(CreateColumnRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        Column column = columnDomainService.createColumn(
                request.getBoardId(),
                request.getTitle(),
                request.getType(),
                currentUserId
        );

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "COLUMN_CREATED");
        payload.put("entityId", column.getId());
        payload.put("boardId", column.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(column.getBoard().getId(), payload));

        return columnMapper.toCreateColumnResponse(column);
    }

    @Override
    public GetColumnResponse getColumn(Long columnId) {
        Column column = columnDomainService.getColumnById(columnId);
        return columnMapper.toGetColumnResponse(column);
    }

    @Override
    public List<GetColumnResponse> getColumnsByBoard(Long boardId) {
        List<Column> columns = columnDomainService.getColumnsByBoardId(boardId);
        return columns.stream()
                .map(columnMapper::toGetColumnResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UpdateColumnResponse updateColumn(UpdateColumnRequest request) {
        Column column = columnDomainService.updateColumnDetails(request.getColumnId(), request.getTitle());

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "COLUMN_UPDATED");
        payload.put("entityId", column.getId());
        payload.put("boardId", column.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(column.getBoard().getId(), payload));

        return columnMapper.toUpdateColumnResponse(column);
    }

    @Override
    public void deleteColumn(DeleteColumnRequest request) {
        // Get column before deletion for event
        Column column = columnDomainService.getColumnById(request.getColumnId());
        Long boardId = column.getBoard().getId();

        Long currentUserId = userContextPort.getCurrentUserId();
        
        columnDomainService.deleteColumn(request.getColumnId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "COLUMN_DELETED");
        payload.put("entityId", request.getColumnId());
        payload.put("boardId", boardId);
        
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }
}
