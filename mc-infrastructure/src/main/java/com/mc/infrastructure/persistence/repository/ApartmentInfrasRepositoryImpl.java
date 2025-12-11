package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.repository.ApartmentRepository;
import com.mc.infrastructure.persistence.mapper.ApartmentJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentInfrasRepositoryImpl implements ApartmentRepository {

    private final ApartmentJPAMapper apartmentJPAMapper;

    @Override
    public Apartment save(Apartment apartment) {
        return apartmentJPAMapper.save(apartment);
    }

}
