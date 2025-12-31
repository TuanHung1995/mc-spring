package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemJPAMapper extends JpaRepository<Item, Long> {

    @Query("SELECT i.position FROM Item i WHERE i.id = :id")
    Double getPosition(Long id);

    @Query("SELECT MAX(i.position) FROM Item i WHERE i.group.id = :groupId")
    Double getMaxPositionByGroupId(Long groupId);

    @Query("SELECT i FROM Item i WHERE i.group.id = :groupId ORDER BY i.position")
    List<Item> findByGroupId(Long groupId);

    @Query("SELECT i FROM Item i WHERE i.board.id = :boardId ORDER BY i.position")
    List<Item> findByBoardId(Long boardId);

    @Query("SELECT i.board.id FROM Item i WHERE i.id = :itemId")
    Optional<Long> findBoardIdByItemId(Long itemId);

}
