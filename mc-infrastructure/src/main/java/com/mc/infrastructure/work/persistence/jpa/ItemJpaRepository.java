package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.ItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ItemJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {

    /** All active items in a group, ordered by position. */
    @Query("SELECT i FROM ItemJpaEntity i WHERE i.groupId = :groupId ORDER BY i.position ASC")
    List<ItemJpaEntity> findByGroupId(@Param("groupId") UUID groupId);

    /** All active items on a board. */
    @Query("SELECT i FROM ItemJpaEntity i WHERE i.boardId = :boardId ORDER BY i.position ASC")
    List<ItemJpaEntity> findByBoardId(@Param("boardId") UUID boardId);

    /** Max position within a group (for append-to-end logic). */
    @Query("SELECT MAX(i.position) FROM ItemJpaEntity i WHERE i.groupId = :groupId")
    Double getMaxPositionByGroupId(@Param("groupId") UUID groupId);

    /** Position of a specific item (for drag-and-drop calculation). */
    @Query("SELECT i.position FROM ItemJpaEntity i WHERE i.id = :id")
    Double getPositionById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE work_items SET is_deleted = true, updated_at = NOW(), deleted_at = NOW(), deleted_by = :deletedById " +
            "WHERE workspace_id = :workspaceId " +
            "AND is_deleted = false " +
            "LIMIT :batchSize", nativeQuery = true)
    int softDeleteByWorkspaceIdInBatch(@Param("workspaceId") UUID workspaceId, @Param("deletedById") UUID deletedById, @Param("batchSize") int batchSize);

}
