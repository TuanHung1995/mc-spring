package com.mc.domain.service.impl;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.enums.RoleType;
import com.mc.domain.repository.RoleRepository;
import com.mc.domain.service.RoleDomainService;
import org.springframework.stereotype.Service;

@Service
public class RoleDomainServiceImpl implements RoleDomainService {

    private final RoleRepository roleRepository;

    public RoleDomainServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefaultRole() {
        return roleRepository.findByName(RoleType.ROLE_ADMIN.name())
                .orElseThrow(() -> new IllegalStateException("Default role " + RoleType.ROLE_ADMIN.name() +  " not found"));
    }
}
