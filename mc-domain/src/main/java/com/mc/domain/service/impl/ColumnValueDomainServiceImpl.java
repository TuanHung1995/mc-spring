package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.ColumnValue;
import com.mc.domain.repository.ColumnValueRepository;
import com.mc.domain.service.ColumnValueDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ColumnValueDomainServiceImpl implements ColumnValueDomainService {

    private final ColumnValueRepository columnValueRepository;

    @Transactional
    public ColumnValue updateColumnValue(Long columnValueId, String newValue, String newColor, String newText) {

        ColumnValue columnValue = columnValueRepository.findById(columnValueId)
                .orElseThrow(() -> new ResourceNotFoundException("ColumnValue", "id", columnValueId));

        if (newValue != null) columnValue.setValue(Collections.singleton(newValue));
        if (newColor != null) columnValue.setColor(newColor);
        if (newText != null) columnValue.setTextValue(newText);

        return columnValueRepository.save(columnValue);
    }


}
