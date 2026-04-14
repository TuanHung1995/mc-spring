package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.VerificationCode;
import com.mc.infrastructure.iam.persistence.model.VerificationCodeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodePersistenceMapper {

    public VerificationCodeJpaEntity toEntity(VerificationCode domain) {
        if (domain == null) return null;

        VerificationCodeJpaEntity entity = new VerificationCodeJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setCode(domain.getCode());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setExpiredAt(domain.getExpiredAt());
        entity.setDeleted(domain.isDeleted());

        return entity;
    }

    public VerificationCode toDomain(VerificationCodeJpaEntity entity) {
        if (entity == null) return null;

        return new VerificationCode(
                entity.getId(),
                entity.getUserId(),
                entity.getCode(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getExpiredAt(),
                entity.isDeleted()
        );
    }
}
