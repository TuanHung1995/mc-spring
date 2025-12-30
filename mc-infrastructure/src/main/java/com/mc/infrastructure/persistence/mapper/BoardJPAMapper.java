package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardJPAMapper extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT b FROM Board b JOIN BoardMember bm ON b.id = bm.board.id WHERE bm.user.id = :userId")
    List<Board> findAllByUserId(@Param("userId") Long userId);

    /**
     * Executes a native SQL DELETE statement to bypass Hibernate's Soft Delete mechanism.
     */
    @Modifying
    @Query(value = "DELETE FROM boards WHERE id = :id", nativeQuery = true)
    void deletePhysical(@Param("id") Long id);

    @Query(value = "SELECT * FROM boards WHERE is_deleted = 1 AND deleted_by = :userId", nativeQuery = true)
    List<Board> findAllDeletedBoards(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE boards SET is_deleted = 0, deleted_at = NULL, deleted_by = NULL WHERE id = :boardId", nativeQuery = true)
    void restoreBoard(@Param("boardId") Long boardId);


}
