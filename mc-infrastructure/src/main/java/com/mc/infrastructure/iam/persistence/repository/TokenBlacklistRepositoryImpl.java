package com.mc.infrastructure.iam.persistence.repository;

import com.mc.domain.iam.model.TokenBlacklist;
import com.mc.domain.iam.repository.TokenBlacklistRepository;
import com.mc.infrastructure.iam.persistence.jpa.TokenBlacklistJpaRepository;
import com.mc.infrastructure.iam.persistence.mapper.TokenBlacklistPersistenceMapper;
import com.mc.infrastructure.iam.persistence.model.TokenBlacklistJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * TokenBlacklist Repository Implementation - Infrastructure Layer
 * Implements the domain TokenBlacklistRepository interface using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class TokenBlacklistRepositoryImpl implements TokenBlacklistRepository {

    private final TokenBlacklistJpaRepository jpaRepository;
    private final TokenBlacklistPersistenceMapper mapper;

    @Override
    public TokenBlacklist save(TokenBlacklist tokenBlacklist) {
        TokenBlacklistJpaEntity entity = mapper.toEntity(tokenBlacklist);
        TokenBlacklistJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TokenBlacklist> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByToken(String token) {
        return jpaRepository.existsByToken(token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(Instant.now());
    }
}
