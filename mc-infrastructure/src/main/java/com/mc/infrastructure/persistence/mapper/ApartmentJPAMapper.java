package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentJPAMapper extends JpaRepository<Apartment, Long> {
}
