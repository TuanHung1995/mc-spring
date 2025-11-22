package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleJPAMapper extends JpaRepository<UserRole, Long> {
}
