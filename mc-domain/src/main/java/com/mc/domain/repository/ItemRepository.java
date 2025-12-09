package com.mc.domain.repository;

import com.mc.domain.model.entity.Item;

import java.util.Optional;

public interface ItemRepository {

    Optional<Item> findById(Long id);
    Item save(Item item);
    Double getPosition(Long itemId);

    Optional<Long> findBoardIdByItemId(Long itemId);

}
