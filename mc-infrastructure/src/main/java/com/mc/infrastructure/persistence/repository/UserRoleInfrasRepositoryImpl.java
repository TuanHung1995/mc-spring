package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.UserRole;
import com.mc.domain.repository.UserRoleDomainRepository;
import com.mc.infrastructure.persistence.mapper.UserRoleJPAMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRoleInfrasRepositoryImpl implements UserRoleDomainRepository {

    private final UserRoleJPAMapper userRoleJPAMapper;

    public UserRoleInfrasRepositoryImpl(UserRoleJPAMapper userRoleJPAMapper) {
        this.userRoleJPAMapper = userRoleJPAMapper;
    }

    @Override
    public void save(UserRole userRole) {
        userRoleJPAMapper.save(userRole);
    }
}
