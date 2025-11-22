package com.mc.domain.repository;

import com.mc.domain.model.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(String name);

}
