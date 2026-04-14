package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.ItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ItemJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, Long> {

    /** All active items in a group, ordered by position. */
    @Query("SELECT i FROM ItemJpaEntity i WHERE i.groupId = :groupId ORDER BY i.position ASC")
    List<ItemJpaEntity> findByGroupId(@Param("groupId") Long groupId);

    /** All active items on a board. */
    @Query("SELECT i FROM ItemJpaEntity i WHERE i.boardId = :boardId ORDER BY i.position ASC")
    List<ItemJpaEntity> findByBoardId(@Param("boardId") Long boardId);

    /** Max position within a group (for append-to-end logic). */
    @Query("SELECT MAX(i.position) FROM ItemJpaEntity i WHERE i.groupId = :groupId")
    Double getMaxPositionByGroupId(@Param("groupId") Long groupId);

    /** Position of a specific item (for drag-and-drop calculation). */
    @Query("SELECT i.position FROM ItemJpaEntity i WHERE i.id = :id")
    Double getPositionById(@Param("id") Long id);
}
