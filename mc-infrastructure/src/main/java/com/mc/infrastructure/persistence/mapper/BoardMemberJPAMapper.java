package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardMemberJPAMapper extends JpaRepository<BoardMember, Long> {

    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId);

}
