package com.mc.infrastructure.iam.persistence.jpa;

import com.mc.infrastructure.iam.persistence.model.VerificationCodeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeJpaRepository extends JpaRepository<VerificationCodeJpaEntity, UUID> {
    
    Optional<VerificationCodeJpaEntity> findByCode(String code);

    Optional<VerificationCodeJpaEntity> findByUserId(UUID userId);

    List<VerificationCodeJpaEntity> findAllByUserId(UUID userId);
}
