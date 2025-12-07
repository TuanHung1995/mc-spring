package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Column;
import com.mc.domain.repository.ColumnRepository;
import com.mc.infrastructure.persistence.mapper.ColumnJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColumnInfrasRepositoryImpl implements ColumnRepository {

    private final ColumnJPAMapper columnJPAMapper;

    @Override
    public Double getPosition(Long columnId) {
        return columnJPAMapper.getPosition(columnId);
    }

    @Override
    public Column save(Column column) {
        return columnJPAMapper.save(column);
    }

    @Override
    public Optional<Column> findById(Long columnId) {
        return columnJPAMapper.findById(columnId);
    }

    @Override
    public Optional<Long> findBoardIdByColumnId(Long columnId) {
        return columnJPAMapper.findBoardIdByColumnId(columnId);
    }

}
