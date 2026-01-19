package com.mc.infrastructure.iam.persistence.model;

import com.mc.domain.model.enums.VerificationCodeStatus;
import com.mc.infrastructure.persistence.model.BaseSimpleJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "verification_codes", indexes = {
        @Index(name = "verification_codes_code_index", columnList = "code"),
        @Index(name = "verification_codes_user_index", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeJpaEntity extends BaseSimpleJpaEntity {

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID userId;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('USED', 'EXPIRED', 'PENDING') DEFAULT 'PENDING'")
    @Enumerated(EnumType.STRING)
    private VerificationCodeStatus status;

    @Column(name = "expired_at")
    private java.time.LocalDateTime expiredAt;

}
