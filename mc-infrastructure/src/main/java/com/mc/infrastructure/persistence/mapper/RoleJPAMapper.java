package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJPAMapper extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
