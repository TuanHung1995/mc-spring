package com.mc.domain.service.impl;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.repository.ItemRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.ItemDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemDomainServiceImpl implements  ItemDomainService {

    private final ItemRepository itemRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PositionCalculationService positionCalculationService;

    @Override
    @Transactional
    public Item createItem(Long boardId, Long groupId, String name, Long userId) {
        // Validate board exists
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        // Validate group exists and belongs to the board
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        
        if (!group.getBoard().getId().equals(boardId)) {
            throw new BusinessLogicException("TaskGroup does not belong to the specified Board");
        }
        
        // Get creator
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Calculate position (append to end of group)
        Double maxPosition = itemRepository.getMaxPositionByGroupId(groupId);
        double newPosition = (maxPosition != null) ? maxPosition + 65536 : 65536;
        
        // Create item
        Item item = new Item();
        item.setName(name);
        item.setBoard(board);
        item.setGroup(group);
        item.setCreatedBy(creator);
        item.setPosition(newPosition);
        item.setCreatedAt(new Date());
        
        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItemsByGroupId(Long groupId) {
        // Validate group exists
        taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        
        return itemRepository.findByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getItemsByBoardId(Long boardId) {
        // Validate board exists
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        return itemRepository.findByBoardId(boardId);
    }

    @Override
    @Transactional
    public Item updateItem(Long itemId, String newName) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if (newName != null) item.setName(newName);

        return itemRepository.save(item);
    }

    @Override
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

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        // Get user who is deleting
        User deleter = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Set soft delete fields
        item.setDeletedAt(new Date());
        item.setDeletedBy(deleter);
        
        // Soft delete
        itemRepository.delete(item);
    }
}
