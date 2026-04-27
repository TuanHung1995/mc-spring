package com.mc.infrastructure.iam.persistence.repository;

import com.mc.domain.iam.model.Role;
import com.mc.domain.iam.model.RoleScope;
import com.mc.domain.iam.repository.RoleRepository;
import com.mc.infrastructure.iam.persistence.jpa.RoleJpaRepository;
import com.mc.infrastructure.iam.persistence.mapper.RolePersistenceMapper;
import com.mc.infrastructure.iam.persistence.model.RoleJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Role Repository Implementation - Infrastructure Layer
 * Implements the domain RoleRepository interface using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;
    private final RolePersistenceMapper mapper;

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = mapper.toEntity(role);
        RoleJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<Role> findByScope(RoleScope scope) {
        return jpaRepository.findByScope(scope.name()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Role role) {
        jpaRepository.deleteById(role.getId());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
