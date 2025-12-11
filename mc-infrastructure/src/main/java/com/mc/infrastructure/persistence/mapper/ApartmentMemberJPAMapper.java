package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.ApartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentMemberJPAMapper extends JpaRepository<ApartmentMember, Long> {
}
