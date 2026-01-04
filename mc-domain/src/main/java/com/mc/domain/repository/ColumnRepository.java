package com.mc.domain.repository;

import com.mc.domain.model.entity.Column;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository {

    Column save(Column column);
    Optional<Column> findById(Long columnId);
    void delete(Column column);
    
    Double getPosition(Long columnId);
    Double getMaxPositionByBoardId(Long boardId);

    Optional<Long> findBoardIdByColumnId(Long columnId);

    List<Column> findAllByBoardId(Long boardId);
}
