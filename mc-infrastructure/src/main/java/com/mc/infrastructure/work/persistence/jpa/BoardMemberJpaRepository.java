package com.mc.infrastructure.work.persistence.jpa;

import com.mc.domain.iam.model.Role;
import com.mc.infrastructure.work.persistence.model.BoardMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardMemberJpaRepository extends JpaRepository<BoardMemberJpaEntity, Long> {

    @Query(value = "SELECT * FROM roles r " +
            "WHERE r.id = (SELECT bm.role_id FROM work_board_members bm " +
            "WHERE bm.board_id = :boardId AND bm.user_id = :userId)",
            nativeQuery = true)
    Optional<Role> findRoleByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") UUID userId);

    boolean existsByBoardIdAndUserId(Long boardId, UUID userId);

}
