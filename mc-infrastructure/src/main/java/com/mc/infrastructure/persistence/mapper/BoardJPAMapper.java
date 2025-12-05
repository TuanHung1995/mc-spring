package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardJPAMapper extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT b FROM Board b JOIN BoardMember bm ON b.id = bm.board.id WHERE bm.user.id = :userId")
    List<Board> findAllByUserId(@Param("userId") Long userId);

}
