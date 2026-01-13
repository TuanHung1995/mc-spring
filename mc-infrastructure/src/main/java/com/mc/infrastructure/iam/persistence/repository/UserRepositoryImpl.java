package com.mc.infrastructure.iam.persistence.repository;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.UserRepository;
import com.mc.infrastructure.iam.persistence.jpa.UserJpaRepository;
import com.mc.infrastructure.iam.persistence.mapper.UserPersistenceMapper;
import com.mc.infrastructure.iam.persistence.model.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User Repository Implementation - Infrastructure Layer
 * Implements the domain UserRepository interface using Spring Data JPA.
 */
@Repository("iamUserRepository")
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }
}
