package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.Permission;
import com.mc.infrastructure.iam.persistence.model.PermissionJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Permission Persistence Mapper - Infrastructure Layer
 * Converts between Permission domain model and PermissionJpaEntity.
 */
@Component
public class PermissionPersistenceMapper {

    /**
     * Convert domain model to JPA entity.
     */
    public PermissionJpaEntity toEntity(Permission domain) {
        if (domain == null) return null;

        PermissionJpaEntity entity = new PermissionJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     */
    public Permission toDomain(PermissionJpaEntity entity) {
        if (entity == null) return null;

        return new Permission(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
