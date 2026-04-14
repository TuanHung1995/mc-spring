package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * BoardJpaRepository — Spring Data JPA Repository (Work Context)
 *
 * <p>Operates on {@link BoardJpaEntity}, keeping all queries within the infrastructure layer.</p>
 */
@Repository
public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {

    /**
     * Returns all boards the given user is a member of (via board_members join table).
     * Uses a native join because board_members is a simple join table.
     */
    @Query(value = "SELECT DISTINCT b.* FROM work_boards b " +
                   "JOIN work_board_members bm ON b.id = bm.board_id " +
                   "WHERE bm.user_id = :userId AND b.is_deleted = 0",
           nativeQuery = true)
    List<BoardJpaEntity> findAllByUserId(@Param("userId") UUID userId);

    /**
     * Finds all boards soft-deleted by a specific user (for the Trash UI).
     */
    @Query(value = "SELECT * FROM work_boards WHERE is_deleted = 1 AND deleted_by = :userId",
           nativeQuery = true)
    List<BoardJpaEntity> findAllTrashedByUserId(@Param("userId") Long userId);

    /**
     * Physically removes a board row, bypassing Hibernate's @SoftDelete.
     * Used for permanent deletion.
     */
    @Modifying
    @Query(value = "DELETE FROM work_boards WHERE id = :id", nativeQuery = true)
    void deletePhysical(@Param("id") Long id);

    /**
     * Restores a soft-deleted board.
     */
    @Modifying
    @Query(value = "UPDATE work_boards SET is_deleted = 0, deleted_at = NULL, deleted_by = NULL WHERE id = :boardId",
           nativeQuery = true)
    void restore(@Param("boardId") Long boardId);
}
