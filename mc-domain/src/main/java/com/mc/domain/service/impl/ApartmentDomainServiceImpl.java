package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.repository.ApartmentRepository;
import com.mc.domain.service.ApartmentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentDomainServiceImpl implements ApartmentDomainService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public Optional<Apartment> findApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId);
    }

}
