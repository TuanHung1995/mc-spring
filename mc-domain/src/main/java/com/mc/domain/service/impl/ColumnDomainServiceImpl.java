package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Column;
import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.ColumnRepository;
import com.mc.domain.repository.ItemRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.service.ColumnDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnDomainServiceImpl implements ColumnDomainService {

    private final ColumnRepository columnRepository;
    private final PositionCalculationService positionCalculationService;

    @Transactional
    public void reorderColumn(Long columnId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? columnRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? columnRepository.getPosition(nextItemId) : null;

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Item
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        // 4. Cập nhật vị trí
        column.setPosition(newPos);
        columnRepository.save(column);

        /*     Next: WebSocket     */

    }

}
