package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardMemberJPAMapper extends JpaRepository<BoardMember, Long> {

    @Query("SELECT bm.role FROM BoardMember bm WHERE bm.board.id = :boardId AND bm.user.id = :userId")
    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId);

}
