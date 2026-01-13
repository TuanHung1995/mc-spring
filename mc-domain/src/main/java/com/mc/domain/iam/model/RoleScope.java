package com.mc.domain.iam.model;

/**
 * RoleScope Enum - Defines the scope/context where a role applies.
 * Part of IAM Bounded Context.
 */
public enum RoleScope {
    SYSTEM,      // System-wide roles (e.g., ADMIN, USER)
    TEAM,        // Team-level roles
    WORKSPACE,   // Workspace-level roles
    BOARD,       // Board-level roles
    APARTMENT    // Apartment-level roles
}
