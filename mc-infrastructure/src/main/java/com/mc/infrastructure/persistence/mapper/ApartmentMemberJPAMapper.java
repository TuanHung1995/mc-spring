package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.ApartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApartmentMemberJPAMapper extends JpaRepository<ApartmentMember, Long> {

    void deleteByUserIdAndApartmentId(Long userId, Long apartmentId);

    Optional<ApartmentMember> findByUserIdAndApartmentId(Long userId, Long apartmentId);

}
