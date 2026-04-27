package com.mc.infrastructure.iam.persistence.repository;

import com.mc.domain.iam.model.VerificationCode;
import com.mc.domain.iam.repository.VerificationCodeRepository;
import com.mc.infrastructure.iam.persistence.jpa.VerificationCodeJpaRepository;
import com.mc.infrastructure.iam.persistence.mapper.VerificationCodePersistenceMapper;
import com.mc.infrastructure.iam.persistence.model.VerificationCodeJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {

    private final VerificationCodeJpaRepository jpaRepository;
    private final VerificationCodePersistenceMapper mapper;

    @Override
    public VerificationCode save(VerificationCode verificationCode) {
        VerificationCodeJpaEntity entity = mapper.toEntity(verificationCode);
        VerificationCodeJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<VerificationCode> findByCode(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<VerificationCode> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public List<VerificationCode> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
