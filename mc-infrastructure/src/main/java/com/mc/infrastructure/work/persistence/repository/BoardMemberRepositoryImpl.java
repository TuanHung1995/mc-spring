package com.mc.infrastructure.work.persistence.repository;

import com.mc.domain.iam.model.Role;
import com.mc.domain.work.model.entity.BoardMember;
import com.mc.domain.work.repository.BoardMemberRepository;
import com.mc.infrastructure.work.persistence.jpa.BoardMemberJpaRepository;
import com.mc.infrastructure.work.persistence.mapper.BoardMemberPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardMemberRepositoryImpl implements BoardMemberRepository {

    private final BoardMemberJpaRepository jpaRepository;
    private final BoardMemberPersistenceMapper mapper;

    @Override
    public BoardMember save(BoardMember member) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(member)));
    }

    @Override
    public Optional<Role> findRoleByBoardIdAndUserId(UUID boardId, UUID userId) {
        return jpaRepository.findRoleByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public boolean existsByBoardIdAndUserId(UUID boardId, UUID userId) {
        return jpaRepository.existsByBoardIdAndUserId(boardId, userId);
    }

}
