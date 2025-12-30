package com.mc.domain.repository;

import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface BoardMemberRepository {

    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId);

    void save(BoardMember boardMember);

    /**
     * Checks if a specific user is a member of a specific board.
     *
     * @param boardId the ID of the board
     * @param userId  the ID of the user
     * @return true if the user is a member, false otherwise
     */
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

}
