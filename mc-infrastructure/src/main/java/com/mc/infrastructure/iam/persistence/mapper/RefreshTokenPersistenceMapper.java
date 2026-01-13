package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.RefreshToken;
import com.mc.infrastructure.iam.persistence.model.RefreshTokenJpaEntity;
import org.springframework.stereotype.Component;

/**
 * RefreshToken Persistence Mapper - Infrastructure Layer
 * Converts between RefreshToken domain model and RefreshTokenJpaEntity.
 */
@Component
public class RefreshTokenPersistenceMapper {

    /**
     * Convert domain model to JPA entity.
     */
    public RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        if (domain == null) return null;

        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUserId(domain.getUserId());
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     */
    public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        if (entity == null) return null;

        return new RefreshToken(
                entity.getId(),
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiryDate(),
                entity.getCreatedAt()
        );
    }
}
