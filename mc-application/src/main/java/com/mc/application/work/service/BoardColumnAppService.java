package com.mc.application.work.service;

import com.mc.application.work.dto.request.CreateColumnRequest;
import com.mc.application.work.dto.response.ColumnResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * BoardColumnAppService — Application Service Port (Work Context)
 */
public interface BoardColumnAppService {

    ColumnResponse createColumn(CreateColumnRequest request);
    ColumnResponse getColumnById(Long columnId);
    List<ColumnResponse> getColumnsByBoard(Long boardId);
    ColumnResponse updateColumnTitle(Long columnId, String newTitle);
    void deleteColumn(Long columnId);
}
