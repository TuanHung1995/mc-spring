package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Permission Repository Interface - Domain Layer
 * Defines persistence operations for Permission entity.
 */
public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(Long id);

    Optional<Permission> findByName(String name);

    Set<Permission> findByNameIn(List<String> names);

    List<Permission> findAll();

    void delete(Permission permission);

    boolean existsByName(String name);
}
