package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.ItemRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.service.TaskGroupDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskGroupDomainServiceImpl implements TaskGroupDomainService {

    private final TaskGroupRepository taskGroupRepository;
    private final PositionCalculationService positionCalculationService;

    @Transactional
    public void reorderGroup(Long targetGroupId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? taskGroupRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? taskGroupRepository.getPosition(nextItemId) : null;

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Group
        TaskGroup group = taskGroupRepository.findById(targetGroupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // 3. Cập nhật Group nếu có thay đổi (Move task sang group khác)
//        if (targetGroupId != null && !targetGroupId.equals(item.getGroup().getId())) {
//            TaskGroup newGroup = taskGroupRepository.findById(targetGroupId)
//                    .orElseThrow(() -> new RuntimeException("Target Group not found"));
//            item.setGroup(newGroup);
//        }

        // 4. Cập nhật vị trí
        group.setPosition(newPos);
        taskGroupRepository.save(group);


        /*     Next: WebSocket     */

    }

}
