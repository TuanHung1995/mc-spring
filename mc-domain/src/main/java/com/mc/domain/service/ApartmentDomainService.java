package com.mc.domain.service;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.ApartmentMember;

import java.util.Optional;

public interface ApartmentDomainService {

    Optional<Apartment> findApartmentById(Long apartmentId);

    void deleteTeam(Long id);

    Optional<ApartmentMember> findApartmentMemberByUserIdAndApartmentId(Long userId, Long apartmentId);

}
