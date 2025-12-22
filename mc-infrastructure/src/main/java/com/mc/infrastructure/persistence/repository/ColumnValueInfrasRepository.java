package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.ColumnValue;
import com.mc.domain.repository.ColumnValueRepository;
import com.mc.infrastructure.persistence.mapper.ColumnValueJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColumnValueInfrasRepository implements ColumnValueRepository {

    private final ColumnValueJPAMapper columnValueJPAMapper;

    @Override
    public ColumnValue save(ColumnValue columnValue) {
        return columnValueJPAMapper.save(columnValue);
    }

    @Override
    public java.util.Optional<ColumnValue> findById(Long columnValueId) {
        return columnValueJPAMapper.findById(columnValueId);
    }

}
