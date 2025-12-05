package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Item;
import com.mc.domain.repository.ItemRepository;
import com.mc.infrastructure.persistence.mapper.ItemJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemInfrasRepositoryImpl implements ItemRepository {

    private final ItemJPAMapper itemJPAMapper;

    @Override
    public Optional<Item> findById(Long id) { return itemJPAMapper.findById(id); }

    @Override
    public Item save(Item item) { return itemJPAMapper.save(item); }

    @Override
    public Double getPosition(Long itemId) { return itemJPAMapper.getPosition(itemId); }
}
