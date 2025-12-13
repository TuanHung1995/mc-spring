package com.mc.domain.repository;

import com.mc.domain.model.entity.Apartment;

import java.util.Optional;

public interface ApartmentRepository {

    Apartment save(Apartment apartment);

    Optional<Apartment> findById(Long apartmentId);

}
