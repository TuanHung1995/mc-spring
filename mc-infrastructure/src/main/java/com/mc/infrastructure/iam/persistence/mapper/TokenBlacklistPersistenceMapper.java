package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.TokenBlacklist;
import com.mc.infrastructure.iam.persistence.model.TokenBlacklistJpaEntity;
import org.springframework.stereotype.Component;

/**
 * TokenBlacklist Persistence Mapper - Infrastructure Layer
 * Converts between TokenBlacklist domain model and TokenBlacklistJpaEntity.
 */
@Component
public class TokenBlacklistPersistenceMapper {

    /**
     * Convert domain model to JPA entity.
     */
    public TokenBlacklistJpaEntity toEntity(TokenBlacklist domain) {
        if (domain == null) return null;

        TokenBlacklistJpaEntity entity = new TokenBlacklistJpaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     */
    public TokenBlacklist toDomain(TokenBlacklistJpaEntity entity) {
        if (entity == null) return null;

        return new TokenBlacklist(
                entity.getId(),
                entity.getToken(),
                entity.getExpiryDate(),
                entity.getCreatedAt()
        );
    }
}
