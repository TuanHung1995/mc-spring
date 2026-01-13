package com.mc.domain.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all persistent entities using UUID.
 * Optimized for Microservices and Distributed Systems.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Stores the User UUID who created this entity.
     * We use UUID instead of Entity reference to decouple modules.
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID createdBy;

    /**
     * Stores the User UUID who last modified this entity.
     */
    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID updatedBy;

    // Cờ xóa mềm (Soft Delete) dùng chung cho toàn hệ thống
    // Bạn có thể đưa vào đây hoặc để từng Entity tự định nghĩa nếu muốn linh hoạt
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    // ========================================================================
    // EQUALS & HASHCODE (UUID Based)
    // ========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
