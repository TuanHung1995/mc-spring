package com.mc.infrastructure.organization.persistence.repository;

import com.mc.domain.organization.model.entity.ApartmentMember;
import com.mc.domain.organization.repository.ApartmentMemberRepository;
import com.mc.infrastructure.organization.persistence.jpa.ApartmentMemberJpaRepository;
import com.mc.infrastructure.organization.persistence.mapper.ApartmentMemberPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ApartmentMemberRepositoryImpl — Persistence Adapter (Organization Context)
 *
 * <p>Implements {@link ApartmentMemberRepository} using Spring Data JPA.
 * This adapter is the ONLY class that knows about {@link com.mc.infrastructure.organization.persistence.model.ApartmentMemberJpaEntity}.
 * All callers above this layer work exclusively with the clean domain model.</p>
 */
@Repository
@RequiredArgsConstructor
public class ApartmentMemberRepositoryImpl implements ApartmentMemberRepository {

    private final ApartmentMemberJpaRepository jpaRepository;
    private final ApartmentMemberPersistenceMapper mapper;

    @Override
    public ApartmentMember save(ApartmentMember member) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(member)));
    }

    @Override
    public Optional<ApartmentMember> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ApartmentMember> findByApartmentIdAndUserId(UUID apartmentId, UUID userId) {
        return jpaRepository.findByApartmentIdAndUserId(apartmentId, userId)
                .map(mapper::toDomain);
    }

    @Override
    public List<ApartmentMember> findAllByApartmentId(UUID apartmentId) {
        return jpaRepository.findAllByApartmentId(apartmentId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentMember> findPendingByApartmentId(UUID apartmentId) {
        return jpaRepository.findAllByApartmentIdAndStatus(apartmentId, "PENDING").stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentMember> findActiveByApartmentId(UUID apartmentId) {
        return jpaRepository.findAllByApartmentIdAndStatus(apartmentId, "ACTIVE").stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByApartmentIdAndUserId(UUID apartmentId, UUID userId) {
        return jpaRepository.existsByApartmentIdAndUserId(apartmentId, userId);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
