package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.ItemRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.service.ItemDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemDomainServiceImpl implements  ItemDomainService {

    private final ItemRepository itemRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final PositionCalculationService positionCalculationService;

    @Transactional
    public Item reorderItem(Long itemId, Long targetGroupId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? itemRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? itemRepository.getPosition(nextItemId) : null;

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        // 3. Cập nhật Group nếu có thay đổi (Move task sang group khác)
        if (targetGroupId != null && !targetGroupId.equals(item.getGroup().getId())) {
            TaskGroup newGroup = taskGroupRepository.findById(targetGroupId)
                    .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", targetGroupId));
            item.setGroup(newGroup);
        }

        // 4. Cập nhật vị trí
        item.setPosition(newPos);
        return itemRepository.save(item);

    }

    @Transactional
    public Item updateItem(Long itemId, String newName) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if (newName != null) item.setName(newName);

        return itemRepository.save(item);

    }
}
