package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.PasswordResetToken;
import com.mc.infrastructure.iam.persistence.model.PasswordResetTokenJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Password Reset Token Persistence Mapper - Infrastructure Layer
 */
@Component
public class PasswordResetTokenPersistenceMapper {

    public PasswordResetToken toDomain(PasswordResetTokenJpaEntity entity) {
        if (entity == null) return null;

        return new PasswordResetToken(
                entity.getId(),
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiryDate(),
                Boolean.TRUE.equals(entity.getUsed()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isDeleted()
        );
    }

    public PasswordResetTokenJpaEntity toEntity(PasswordResetToken domain) {
        if (domain == null) return null;

        PasswordResetTokenJpaEntity entity = new PasswordResetTokenJpaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUserId(domain.getUserId());
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setUsed(domain.isUsed());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeleted(domain.isDeleted());

        return entity;
    }
}
