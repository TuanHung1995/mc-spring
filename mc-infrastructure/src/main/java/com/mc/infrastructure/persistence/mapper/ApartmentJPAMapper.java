package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentJPAMapper extends JpaRepository<Apartment, Long> {

    List<Apartment> findAllByWorkspaceId(Long workspaceId);

}
