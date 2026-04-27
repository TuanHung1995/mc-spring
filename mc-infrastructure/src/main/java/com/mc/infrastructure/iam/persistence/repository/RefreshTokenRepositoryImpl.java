package com.mc.infrastructure.iam.persistence.repository;

import com.mc.domain.iam.model.RefreshToken;
import com.mc.domain.iam.repository.RefreshTokenRepository;
import com.mc.infrastructure.iam.persistence.jpa.RefreshTokenJpaRepository;
import com.mc.infrastructure.iam.persistence.mapper.RefreshTokenPersistenceMapper;
import com.mc.infrastructure.iam.persistence.model.RefreshTokenJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * RefreshToken Repository Implementation - Infrastructure Layer
 * Implements the domain RefreshTokenRepository interface using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenPersistenceMapper mapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entity = mapper.toEntity(refreshToken);
        RefreshTokenJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        jpaRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteByUserId(UUID userId) {
        jpaRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(Instant.now());
    }
}
