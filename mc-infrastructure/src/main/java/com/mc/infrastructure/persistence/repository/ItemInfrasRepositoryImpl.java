package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Item;
import com.mc.domain.repository.ItemRepository;
import com.mc.infrastructure.persistence.mapper.ItemJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemInfrasRepositoryImpl implements ItemRepository {

    private final ItemJPAMapper itemJPAMapper;

    @Override
    public Optional<Item> findById(Long id) { 
        return itemJPAMapper.findById(id); 
    }

    @Override
    public Item save(Item item) { 
        return itemJPAMapper.save(item); 
    }

    @Override
    public void delete(Item item) {
        if (item.getDeletedAt() != null || item.getDeletedBy() != null) {
            itemJPAMapper.saveAndFlush(item);
        }
        itemJPAMapper.delete(item); 
    }

    @Override
    public List<Item> findByGroupId(Long groupId) {
        return itemJPAMapper.findByGroupId(groupId);
    }

    @Override
    public List<Item> findByBoardId(Long boardId) {
        return itemJPAMapper.findByBoardId(boardId);
    }

    @Override
    public Double getPosition(Long itemId) { 
        return itemJPAMapper.getPosition(itemId); 
    }

    @Override
    public Double getMaxPositionByGroupId(Long groupId) {
        return itemJPAMapper.getMaxPositionByGroupId(groupId);
    }

    @Override
    public Optional<Long> findBoardIdByItemId(Long itemId) {
        return itemJPAMapper.findBoardIdByItemId(itemId);
    }
}
