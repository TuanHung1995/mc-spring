package com.mc.domain.work.repository;

import com.mc.domain.work.model.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ItemRepository — Domain Repository Port (Work Context)
 */
public interface ItemRepository {

    Item save(Item item);

    void saveAll(List<Item> items);

    Optional<Item> findById(UUID itemId);

    /** Returns all active items for a task group, ordered by position. */
    List<Item> findByGroupId(UUID groupId);

    /** Returns all active items for a board. */
    List<Item> findByBoardId(UUID boardId);

    /** Returns the max position within a group (for append-to-end logic). */
    Double getMaxPositionByGroupId(UUID groupId);

    /** Returns the position of a specific item (for reorder calculations). */
    Double getPositionById(UUID itemId);

    /** Soft-deletes an item. */
    void delete(Item item);

    int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize);
}
