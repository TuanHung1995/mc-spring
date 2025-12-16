package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;
import com.mc.domain.repository.ApartmentMemberRepository;
import com.mc.domain.repository.ApartmentRepository;
import com.mc.domain.service.ApartmentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
