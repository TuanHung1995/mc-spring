package com.mc.application.work.service;

import com.mc.application.work.dto.request.CreateItemRequest;
import com.mc.application.work.dto.response.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ItemAppService — Application Service Port (Work Context)
 */
public interface ItemAppService {

    ItemResponse createItem(CreateItemRequest request);
    ItemResponse getItemById(Long itemId);
    List<ItemResponse> getItemsByGroup(Long groupId);
    List<ItemResponse> getItemsByBoard(Long boardId);
    ItemResponse updateItemName(Long itemId, String newName);
    void deleteItem(Long itemId);
}
