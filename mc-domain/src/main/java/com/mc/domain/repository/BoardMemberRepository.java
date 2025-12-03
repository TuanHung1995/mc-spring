package com.mc.domain.repository;

import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface BoardMemberRepository {

    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId);

    void save(BoardMember boardMember);
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

}
