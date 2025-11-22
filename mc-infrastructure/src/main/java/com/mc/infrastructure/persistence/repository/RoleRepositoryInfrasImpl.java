package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.Role;
import com.mc.domain.repository.RoleRepository;
import com.mc.infrastructure.persistence.mapper.RoleJPAMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleRepositoryInfrasImpl implements RoleRepository {

    private final RoleJPAMapper roleJPAMapper;

    public RoleRepositoryInfrasImpl(RoleJPAMapper roleJPAMapper) {
        this.roleJPAMapper = roleJPAMapper;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJPAMapper.findByName(name);
    }
}
