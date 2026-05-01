package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.ColumnValueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ColumnValueJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface ColumnValueJpaRepository extends JpaRepository<ColumnValueJpaEntity, Long> {

    @Query("SELECT cv FROM ColumnValueJpaEntity cv WHERE cv.itemId = :itemId")
    List<ColumnValueJpaEntity> findByItemId(@Param("itemId") UUID itemId);

    @Query("SELECT cv FROM ColumnValueJpaEntity cv WHERE cv.boardId = :boardId")
    List<ColumnValueJpaEntity> findByBoardId(@Param("boardId") UUID boardId);

    @Modifying
    @Query(value = "UPDATE work_column_values SET is_deleted = true, updated_at = NOW(), deleted_at = NOW(), deleted_by = :deletedById " +
            "WHERE workspace_id = :workspaceId " +
            "AND is_deleted = false " +
            "LIMIT :batchSize", nativeQuery = true)
    int softDeleteByWorkspaceIdInBatch(@Param("workspaceId") UUID workspaceId, @Param("deletedById") UUID deletedById, @Param("batchSize") int batchSize);

}
