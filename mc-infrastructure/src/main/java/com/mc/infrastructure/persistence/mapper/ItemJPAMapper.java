package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemJPAMapper extends JpaRepository<Item, Long> {

    @Query("SELECT i.position FROM Item i WHERE i.id = :id")
    Double getPosition(Long id);

    @Query("SELECT i.board.id FROM Item i WHERE i.id = :itemId")
    Optional<Long> findBoardIdByItemId(Long itemId);

}
