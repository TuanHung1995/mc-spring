package com.mc.domain.service;

import com.mc.domain.model.entity.Apartment;

import java.util.Optional;

public interface ApartmentDomainService {

    Optional<Apartment> findApartmentById(Long apartmentId);

    void deleteTeam(Long id);

}
