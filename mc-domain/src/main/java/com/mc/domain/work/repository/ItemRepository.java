package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * ItemRepository — Domain Repository Port (Work Context)
 */
@Component("workItemRepository")
public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long itemId);

    /** Returns all active items for a task group, ordered by position. */
    List<Item> findByGroupId(Long groupId);

    /** Returns all active items for a board. */
    List<Item> findByBoardId(Long boardId);

    /** Returns the max position within a group (for append-to-end logic). */
    Double getMaxPositionByGroupId(Long groupId);

    /** Returns the position of a specific item (for reorder calculations). */
    Double getPositionById(Long itemId);

    /** Soft-deletes an item. */
    void delete(Item item);
}
