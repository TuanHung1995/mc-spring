package com.mc.domain.repository;

import com.mc.domain.model.entity.Column;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository {

    Column save(Column column);
    Optional<Column> findById(Long columnId);
    Double getPosition(Long columnId);

    Optional<Long> findBoardIdByColumnId(Long columnId);

    List<Column> findAllByBoardId(Long boardId);
}
