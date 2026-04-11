package com.mc.infrastructure.work.persistence.jpa;

import com.mc.infrastructure.work.persistence.model.ColumnValueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ColumnValueJpaRepository — Spring Data JPA Repository (Work Context)
 */
@Repository
public interface ColumnValueJpaRepository extends JpaRepository<ColumnValueJpaEntity, Long> {

    @Query("SELECT cv FROM ColumnValueJpaEntity cv WHERE cv.itemId = :itemId")
    List<ColumnValueJpaEntity> findByItemId(@Param("itemId") Long itemId);

    @Query("SELECT cv FROM ColumnValueJpaEntity cv WHERE cv.boardId = :boardId")
    List<ColumnValueJpaEntity> findByBoardId(@Param("boardId") Long boardId);
}
