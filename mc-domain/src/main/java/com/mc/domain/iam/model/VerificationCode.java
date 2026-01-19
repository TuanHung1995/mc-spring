package com.mc.domain.iam.model;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.model.BaseEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.model.enums.VerificationCodeStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCode extends BaseDomainEntity {

    private UUID userId;
    private String code;
    private VerificationCodeStatus status;
    private LocalDateTime expiredAt;

    public VerificationCode(UUID id, UUID userId, String code, VerificationCodeStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiredAt, boolean isDeleted) {
        super(id, createdAt, updatedAt, isDeleted);
        this.userId = userId;
        this.code = code;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public static VerificationCode create(UUID userId, String code, LocalDateTime expiredAt) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.initializeNewEntity(IdUtils.newId());
        verificationCode.userId = userId;
        verificationCode.code = code;
        verificationCode.status = VerificationCodeStatus.PENDING;
        verificationCode.expiredAt = expiredAt;
        return verificationCode;
    }

    public void markAsUsed() {
        this.status = VerificationCodeStatus.USED;
        markAsModified();
    }

    public void markAsExpired() {
        this.status = VerificationCodeStatus.EXPIRED;
        markAsModified();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public boolean isUsed() {
        return this.status == VerificationCodeStatus.USED;
    }
}
