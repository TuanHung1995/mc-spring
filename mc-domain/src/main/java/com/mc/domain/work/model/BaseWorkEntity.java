package com.mc.domain.work.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * BaseWorkEntity — Base class for Work Context domain entities (Long ID).
 *
 * <p>The Work context (Board, TaskGroup, Column, Item) uses {@code BIGINT AUTO_INCREMENT}
 * primary keys in MySQL — a different identity strategy from the Organization context
 * which uses {@code BINARY(16)} (UUID). This separate base class avoids misusing
 * {@link com.mc.domain.core.model.BaseDomainEntity} which is UUID-typed.</p>
 *
 * <p>Provides: Long identity, audit timestamps, soft-delete flag.</p>
 *
 * <p><strong>NO JPA annotations here</strong> — this is a pure domain base class.</p>
 */
@Getter
public abstract class BaseWorkEntity {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

    // =================================================================
    // CONSTRUCTORS
    // =================================================================

    protected BaseWorkEntity() {}

    /**
     * Reconstitution constructor — used by persistence mappers
     * to re-hydrate existing entities from the database.
     */
    protected BaseWorkEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    // =================================================================
    // MUTATION HELPERS
    // =================================================================

    protected void setId(Long id) {
        this.id = id;
    }

    /**
     * Initializes a brand-new entity (called from factory methods).
     * Sets the provided ID and stamps both timestamps to now.
     */
    protected void initNew(Long id) {
        this.id = id; // May be null for auto-generated; DB will assign on INSERT
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = false;
    }

    /** Call after any state mutation to update the updatedAt timestamp. */
    protected void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Marks this entity as soft-deleted. */
    public void markDeleted() {
        this.deleted = true;
        touch();
    }

    /** Restores a soft-deleted entity. */
    public void restore() {
        this.deleted = false;
        touch();
    }

    // =================================================================
    // EQUALITY (Long ID-based)
    // =================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseWorkEntity that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + "}";
    }
}
