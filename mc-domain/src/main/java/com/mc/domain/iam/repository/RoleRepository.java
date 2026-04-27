package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.Role;
import com.mc.domain.iam.model.RoleScope;

import java.util.List;
import java.util.Optional;

/**
 * Role Repository Interface - Domain Layer
 * Defines persistence operations for Role aggregate.
 */
public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    List<Role> findByScope(RoleScope scope);

    List<Role> findAll();

    void delete(Role role);

    boolean existsByName(String name);
}
