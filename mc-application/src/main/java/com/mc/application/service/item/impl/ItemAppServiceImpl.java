package com.mc.application.service.item.impl;

import com.mc.application.mapper.ItemMapper;
import com.mc.application.model.item.*;
import com.mc.application.service.item.ItemAppService;
import com.mc.domain.event.BoardChangedEvent;
import com.mc.domain.model.entity.Item;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.ItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemAppServiceImpl implements ItemAppService {

    private final ItemDomainService itemDomainService;
    private final ItemMapper itemMapper;
    private final UserContextPort userContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CreateItemResponse createItem(CreateItemRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        Item item = itemDomainService.createItem(
                request.getBoardId(),
                request.getGroupId(),
                request.getName(),
                currentUserId
        );

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ITEM_CREATED");
        payload.put("entityId", item.getId());
        payload.put("boardId", item.getBoard().getId());
        payload.put("groupId", item.getGroup().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(item.getBoard().getId(), payload));

        return itemMapper.toCreateItemResponse(item);
    }

    @Override
    public GetItemResponse getItem(Long itemId) {
        Item item = itemDomainService.getItemById(itemId);
        return itemMapper.toGetItemResponse(item);
    }

    @Override
    public List<GetItemResponse> getItemsByGroup(Long groupId) {
        List<Item> items = itemDomainService.getItemsByGroupId(groupId);
        return items.stream()
                .map(itemMapper::toGetItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetItemResponse> getItemsByBoard(Long boardId) {
        List<Item> items = itemDomainService.getItemsByBoardId(boardId);
        return items.stream()
                .map(itemMapper::toGetItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UpdateItemResponse updateItem(UpdateItemRequest request) {
        Item item = itemDomainService.updateItem(request.getItemId(), request.getName());

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ITEM_UPDATED");
        payload.put("entityId", item.getId());
        payload.put("boardId", item.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(item.getBoard().getId(), payload));

        return itemMapper.toUpdateItemResponse(item);
    }

    @Override
    public void deleteItem(DeleteItemRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        // Get item before deletion for event
        Item item = itemDomainService.getItemById(request.getItemId());
        Long boardId = item.getBoard().getId();
        
        itemDomainService.deleteItem(request.getItemId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ITEM_DELETED");
        payload.put("entityId", request.getItemId());
        payload.put("boardId", boardId);
        
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }
}
