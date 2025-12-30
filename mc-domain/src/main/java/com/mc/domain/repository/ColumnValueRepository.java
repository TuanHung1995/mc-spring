package com.mc.domain.repository;

import com.mc.domain.model.entity.ColumnValue;

import java.util.Optional;

public interface ColumnValueRepository {

    ColumnValue save(ColumnValue columnValue);

    Optional<ColumnValue> findById(Long columnValueId);

}
