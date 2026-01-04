package com.mc.domain.repository;

import com.mc.domain.model.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> findById(Long id);
    Item save(Item item);
    void delete(Item item);
    
    List<Item> findByGroupId(Long groupId);
    List<Item> findByBoardId(Long boardId);
    
    Double getPosition(Long itemId);
    Double getMaxPositionByGroupId(Long groupId);

    Optional<Long> findBoardIdByItemId(Long itemId);

}
