package com.mc.domain.service;

import com.mc.domain.model.entity.Item;

import java.util.List;

public interface ItemDomainService {

    // Create operations
    Item createItem(Long boardId, Long groupId, String name, Long userId);

    // Read operations
    Item getItemById(Long itemId);
    List<Item> getItemsByGroupId(Long groupId);
    List<Item> getItemsByBoardId(Long boardId);

    // Update operations
    Item updateItem(Long itemId, String newName);
    Item reorderItem(Long itemId, Long targetGroup, Long prevItemId, Long nextItemId);

    // Delete operations
    void deleteItem(Long itemId, Long userId);

}
