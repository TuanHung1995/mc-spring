package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface BoardMemberRepository {

    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId);

}
