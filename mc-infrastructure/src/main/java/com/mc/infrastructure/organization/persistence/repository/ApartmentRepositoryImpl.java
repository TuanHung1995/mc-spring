package com.mc.infrastructure.organization.persistence.repository;

import com.mc.domain.organization.model.entity.Apartment;
import com.mc.domain.organization.repository.ApartmentRepository;
import com.mc.infrastructure.organization.persistence.jpa.ApartmentJpaRepository;
import com.mc.infrastructure.organization.persistence.mapper.ApartmentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ApartmentRepositoryImpl — Persistence Adapter (Organization Context)
 *
 * <p>Implements the domain's {@link ApartmentRepository} port using Spring Data JPA.
 * All IDs are UUID, matching the {@code org_apartments.id BINARY(16)} column.</p>
 */
@Repository
@RequiredArgsConstructor
public class ApartmentRepositoryImpl implements ApartmentRepository {

    private final ApartmentJpaRepository apartmentJpaRepository;
    private final ApartmentPersistenceMapper apartmentPersistenceMapper;

    @Override
    public Apartment save(Apartment apartment) {
        return apartmentPersistenceMapper.toDomain(
                apartmentJpaRepository.save(apartmentPersistenceMapper.toEntity(apartment))
        );
    }

    @Override
    public Optional<Apartment> findById(UUID id) {
        return apartmentJpaRepository.findById(id)
                .map(apartmentPersistenceMapper::toDomain);
    }

    @Override
    public List<Apartment> findAllByWorkspaceId(UUID workspaceId) {
        return apartmentJpaRepository.findAllByWorkspaceId(workspaceId).stream()
                .map(apartmentPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        apartmentJpaRepository.deleteById(id);
    }

    @Override
    public void softDeleteByWorkspaceId(UUID workspaceId, UUID deletedBy) {
        apartmentJpaRepository.softDeleteByWorkspaceId(workspaceId, deletedBy);
    }
}
