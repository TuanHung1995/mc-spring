package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.repository.BoardMemberRepository;
import com.mc.infrastructure.persistence.mapper.BoardMemberJPAMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardMemberInfrasRepository implements BoardMemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final BoardMemberJPAMapper boardMemberJPAMapper;

    @Override
    public Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId) {
        try {
            return boardMemberJPAMapper
                    .findRoleByBoardIdAndUserId(boardId, userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
