package com.mc.domain.iam.model;

import com.mc.domain.exception.DomainException;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Role Domain Model - Pure Domain (no JPA dependencies)
 * Represents a role with associated permissions in the system.
 */
@Getter
public class Role {
    
    private Long id;
    private String name;
    private RoleScope scope;
    private Set<Permission> permissions;

    // =================================================================
    // CONSTRUCTOR (For Persistence)
    // =================================================================
    public Role(Long id, String name, RoleScope scope, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.scope = scope;
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
    }

    private Role() {}

    // =================================================================
    // FACTORY METHODS (Creation)
    // =================================================================
    public static Role create(String name, RoleScope scope) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Role name cannot be empty");
        }
        Role role = new Role();
        role.name = name.trim().toUpperCase();
        role.scope = scope;
        role.permissions = new HashSet<>();
        return role;
    }

    // =================================================================
    // BUSINESS LOGIC (Behavior)
    // =================================================================
    public void addPermission(Permission permission) {
        if (permission == null) {
            throw new DomainException("Permission cannot be null");
        }
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public boolean hasPermission(String permissionName) {
        return this.permissions.stream()
                .anyMatch(p -> p.getName().equals(permissionName));
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(this.permissions);
    }
}
