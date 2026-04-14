package com.mc.domain.iam.repository;

import com.mc.domain.iam.model.VerificationCode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository {
    
    VerificationCode save(VerificationCode verificationCode);
    
    Optional<VerificationCode> findByCode(String code);
    
    Optional<VerificationCode> findByUserId(UUID userId);

    List<VerificationCode> findAllByUserId(UUID userId);

}
