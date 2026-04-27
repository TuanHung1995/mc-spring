package com.mc.application.work.service.impl;

import com.mc.application.work.dto.request.CreateItemRequest;
import com.mc.application.work.dto.response.ItemResponse;
import com.mc.application.work.service.ItemAppService;
import com.mc.domain.core.event.BoardChangedEvent;
import com.mc.domain.core.exception.BusinessLogicException;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.work.port.WorkUserContextPort;
import com.mc.domain.work.model.entity.Item;
import com.mc.domain.work.model.entity.TaskGroup;
import com.mc.domain.work.repository.ItemRepository;
import com.mc.domain.work.repository.TaskGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ItemAppServiceImpl — Application Service (Work Context)
 */
@Service
@RequiredArgsConstructor
public class ItemAppServiceImpl implements ItemAppService {

    private final ItemRepository itemRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final WorkUserContextPort workUserContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ItemResponse createItem(CreateItemRequest request) {
        UUID userId = workUserContextPort.getCurrentUser().id();

        // Validate group exists and belongs to the requested board
        TaskGroup group = taskGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", request.getGroupId()));

        if (!group.getBoardId().equals(request.getBoardId())) {
            throw new BusinessLogicException("TaskGroup does not belong to the specified Board");
        }

        Double maxPos = itemRepository.getMaxPositionByGroupId(request.getGroupId());
        double newPos = (maxPos != null) ? maxPos + 65536 : 65536;

        Item item = Item.create(request.getBoardId(), request.getGroupId(),
                request.getName(), newPos, userId);
        Item saved = itemRepository.save(item);

//        publishEvent("ITEM_CREATED", saved.getId(), saved.getBoardId(), saved.getGroupId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse getItemById(UUID itemId) {
        return toResponse(requireItem(itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByGroup(UUID groupId) {
        return itemRepository.findByGroupId(groupId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByBoard(UUID boardId) {
        return itemRepository.findByBoardId(boardId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemResponse updateItemName(UUID itemId, String newName) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        Item item = requireItem(itemId);
        item.rename(newName, userId);
        Item saved = itemRepository.save(item);
//        publishEvent("ITEM_UPDATED", saved.getId(), saved.getBoardId(), saved.getGroupId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteItem(UUID itemId) {
        UUID userId = workUserContextPort.getCurrentUser().id();
        Item item = requireItem(itemId);
        UUID boardId = item.getBoardId();
        item.trash(userId);
        itemRepository.delete(item);
//        publishEvent("ITEM_DELETED", itemId, boardId, null);
    }

    // =================================================================
    // HELPERS
    // =================================================================

    private Item requireItem(UUID itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
    }

    private void publishEvent(String type, Long entityId, Long boardId, Long groupId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        payload.put("entityId", entityId);
        payload.put("boardId", boardId);
        if (groupId != null) payload.put("groupId", groupId);
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    private ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getBoardId(), item.getGroupId(),
                item.getName(), item.getPosition(), item.getCreatedById(),
                item.getCreatedAt(), item.getUpdatedAt());
    }
}
