package com.mc.domain.work.repository;


import com.mc.domain.iam.model.Role;
import com.mc.domain.work.model.entity.BoardMember;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("workBoardMemberRepository")
public interface BoardMemberRepository {

    Optional<Role> findRoleByBoardIdAndUserId(Long boardId, UUID userId);

    BoardMember save(BoardMember boardMember);

    /**
     * Checks if a specific user is a member of a specific board.
     *
     * @param boardId the ID of the board
     * @param userId  the ID of the user
     * @return true if the user is a member, false otherwise
     */
    boolean existsByBoardIdAndUserId(Long boardId, UUID userId);

}
