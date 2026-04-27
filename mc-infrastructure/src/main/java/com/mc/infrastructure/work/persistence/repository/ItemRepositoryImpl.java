package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.work.model.entity.Item;
import com.mc.domain.work.repository.ItemRepository;
import com.mc.infrastructure.work.persistence.jpa.ItemJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.ItemPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ItemRepositoryImpl — Persistence Adapter (Work Context)
 */
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final ItemJpaRepository jpaRepository;
    private final ItemPersistenceMapper mapper;

    @Override
    public Item save(Item item) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(item)));
    }

    @Override
    public Optional<Item> findById(UUID itemId) {
        return jpaRepository.findById(itemId).map(mapper::toDomain);
    }

    @Override
    public List<Item> findByGroupId(UUID groupId) {
        return jpaRepository.findByGroupId(groupId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Item> findByBoardId(UUID boardId) {
        return jpaRepository.findByBoardId(boardId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Double getMaxPositionByGroupId(UUID groupId) {
        return jpaRepository.getMaxPositionByGroupId(groupId);
    }

    @Override
    public Double getPositionById(UUID itemId) {
        return jpaRepository.getPositionById(itemId);
    }

    @Override
    public void delete(Item item) {
        jpaRepository.saveAndFlush(mapper.toEntity(item));
        jpaRepository.delete(mapper.toEntity(item));
    }

    @Override
    public int softDeleteByWorkspaceIdInBatch(UUID workspaceId, UUID deletedById, int batchSize) {
        return jpaRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }
}
