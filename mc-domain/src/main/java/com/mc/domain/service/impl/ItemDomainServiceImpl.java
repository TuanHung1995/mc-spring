package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.ItemRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.service.ItemDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemDomainServiceImpl implements  ItemDomainService {

    private final ItemRepository itemRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final PositionCalculationService positionCalculationService;

    @Transactional
    public void reorderItem(Long itemId, Long targetGroupId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? itemRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? itemRepository.getPosition(nextItemId) : null;

        log.info("PREV POSI AND NEXT POSI: {} {}", prevPos, nextPos);

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // 3. Cập nhật Group nếu có thay đổi (Move task sang group khác)
        if (targetGroupId != null && !targetGroupId.equals(item.getGroup().getId())) {
            TaskGroup newGroup = taskGroupRepository.findById(targetGroupId)
                    .orElseThrow(() -> new RuntimeException("Target Group not found"));
            item.setGroup(newGroup);
        }



        // 4. Cập nhật vị trí
        item.setPosition(newPos);

        // new item
        log.info("NEW ITEM: {}", item);

        itemRepository.save(item);


        /*     Next: WebSocket     */

    }
}
