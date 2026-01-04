package com.mc.domain.service;

import com.mc.domain.model.entity.Column;
import com.mc.domain.model.enums.BoardColumnType;

import java.util.List;
import java.util.Optional;

public interface ColumnDomainService {

    // Create operations
    Column createColumn(Long boardId, String title, BoardColumnType type, Long userId);

    // Read operations
    Column getColumnById(Long columnId);
    List<Column> getColumnsByBoardId(Long boardId);

    // Update operations
    Column updateColumnDetails(Long columnId, String newTitle);
    Column reorderColumn(Long columnId, Long prevItemId, Long nextItemId);

    // Delete operations
    void deleteColumn(Long columnId, Long userId);

}
