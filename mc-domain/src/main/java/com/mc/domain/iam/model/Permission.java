package com.mc.domain.iam.model;

import com.mc.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Permission Domain Model - Pure Domain (no JPA dependencies)
 * Represents a permission that can be assigned to roles.
 */
@Getter
@EqualsAndHashCode(of = "name")
public class Permission {
    
    private Long id;
    private String name;
    private String description;

    // =================================================================
    // CONSTRUCTOR (For Persistence)
    // =================================================================
    public Permission(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    private Permission() {}

    // =================================================================
    // FACTORY METHODS (Creation)
    // =================================================================
    public static Permission create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Permission name cannot be empty");
        }
        Permission permission = new Permission();
        permission.name = name.trim().toUpperCase();
        permission.description = description;
        return permission;
    }

    // =================================================================
    // BUSINESS LOGIC
    // =================================================================
    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }
}
