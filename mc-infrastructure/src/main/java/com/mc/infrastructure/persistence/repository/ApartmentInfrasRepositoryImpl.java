package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Apartment;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.ApartmentRepository;
import com.mc.infrastructure.persistence.mapper.ApartmentJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentInfrasRepositoryImpl implements ApartmentRepository {

    private final ApartmentJPAMapper apartmentJPAMapper;

    @Override
    public Apartment save(Apartment apartment) {
        return apartmentJPAMapper.save(apartment);
    }

    @Override
    public Optional<Apartment> findById(Long apartmentId) {
        return apartmentJPAMapper.findById(apartmentId);
    }

    @Override
    public void deleteById(Long id) {
        apartmentJPAMapper.deleteById(id);
    }

}
