package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.Permission;
import com.mc.domain.iam.model.Role;
import com.mc.domain.iam.model.RoleScope;
import com.mc.infrastructure.iam.persistence.model.PermissionJpaEntity;
import com.mc.infrastructure.iam.persistence.model.RoleJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Role Persistence Mapper - Infrastructure Layer
 * Converts between Role domain model and RoleJpaEntity.
 */
@Component
@RequiredArgsConstructor
public class RolePersistenceMapper {

    private final PermissionPersistenceMapper permissionMapper;

    /**
     * Convert domain model to JPA entity.
     */
    public RoleJpaEntity toEntity(Role domain) {
        if (domain == null) return null;

        RoleJpaEntity entity = new RoleJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setScope(domain.getScope().name());

        Set<PermissionJpaEntity> permissionEntities = domain.getPermissions().stream()
                .map(permissionMapper::toEntity)
                .collect(Collectors.toSet());
        entity.setPermissions(permissionEntities);

        return entity;
    }

    /**
     * Convert JPA entity to domain model.
     */
    public Role toDomain(RoleJpaEntity entity) {
        if (entity == null) return null;

        Set<Permission> permissions = entity.getPermissions().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toSet());

        return new Role(
                entity.getId(),
                entity.getName(),
                RoleScope.valueOf(entity.getScope()),
                permissions
        );
    }
}
