package com.mc.application.work.service;

import com.mc.application.work.dto.request.CreateItemRequest;
import com.mc.application.work.dto.response.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * ItemAppService — Application Service Port (Work Context)
 */
public interface ItemAppService {

    ItemResponse createItem(CreateItemRequest request);
    ItemResponse getItemById(UUID itemId);
    List<ItemResponse> getItemsByGroup(UUID groupId);
    List<ItemResponse> getItemsByBoard(UUID boardId);
    ItemResponse updateItemName(UUID itemId, String newName);
    void deleteItem(UUID itemId);
}
