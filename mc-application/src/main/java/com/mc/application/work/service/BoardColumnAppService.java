package com.mc.application.work.service;

import com.mc.application.work.dto.request.CreateColumnRequest;
import com.mc.application.work.dto.response.ColumnResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * BoardColumnAppService — Application Service Port (Work Context)
 */
public interface BoardColumnAppService {

    ColumnResponse createColumn(CreateColumnRequest request);
    ColumnResponse getColumnById(Long columnId);
    List<ColumnResponse> getColumnsByBoard(UUID boardId);
    ColumnResponse updateColumnTitle(Long columnId, String newTitle);
    void deleteColumn(Long columnId);
}
