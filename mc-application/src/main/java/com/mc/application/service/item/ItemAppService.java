package com.mc.application.service.item;

import com.mc.application.model.item.*;

import java.util.List;

public interface ItemAppService {

    CreateItemResponse createItem(CreateItemRequest request);

    GetItemResponse getItem(Long itemId);

    List<GetItemResponse> getItemsByGroup(Long groupId);

    List<GetItemResponse> getItemsByBoard(Long boardId);

    UpdateItemResponse updateItem(UpdateItemRequest request);

    void deleteItem(DeleteItemRequest request);

}
