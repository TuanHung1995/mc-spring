package com.mc.domain.service.impl;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.repository.ApartmentMemberRepository;
import com.mc.domain.repository.ApartmentRepository;
import com.mc.domain.service.ApartmentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentDomainServiceImpl implements ApartmentDomainService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMemberRepository apartmentMemberRepository;

    @Override
    public Optional<Apartment> findApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId);
    }

    @Override
    public void deleteTeam(Long id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId) {
        return apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(userId, apartmentId);
    }

    @Override
    @Transactional
    public void updateApartment(Long apartmentId, Long requesterId, String apartmentName, String description, String avatarUrl, String backgroundImageUrl, Boolean isPrivate) {
        // 1. Verify authorization (Must be an OWNER)
        ApartmentMember member = apartmentMemberRepository.findApartmentMemberByUserIdAndApartmentId(requesterId, apartmentId)
                .orElseThrow(() -> new BusinessLogicException("User is not a member of this apartment"));

        if (!RoleType.ROLE_OWNER_APARTMENT.name().equals(member.getRole().getName())) {
            throw new BusinessLogicException("Only the apartment OWNER can update its settings.");
        }

        // 2. Retrieve the entity
        Apartment apartment = member.getApartment();

        // 3. Apply updates (Partial update strategy)
        if (apartmentName != null && !apartmentName.isBlank()) {
            apartment.setName(apartmentName);
        }

        if (description != null) {
            apartment.setDescription(description);
        }

//        if (avatarUrl != null) {
//            apartment.setAvatarUrl(avatarUrl);
//        }

        if (backgroundImageUrl != null) {
            apartment.setBackgroundUrl(backgroundImageUrl);
        }

//        if (isPrivate != null) {
//            apartment.setIsPrivate(isPrivate);
//        }

        // 4. Persist changes
        apartmentRepository.save(apartment);
    }

    @Override
    public List<Apartment> getAllByWorkspaceId(Long workspaceId) {
        return apartmentRepository.findAllByWorkspaceId(workspaceId);
    }

    @Override
    public Apartment getApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId).orElseThrow(() -> new BusinessLogicException("Apartment not found with id: " + apartmentId));
    }

}
