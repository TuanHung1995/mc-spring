package com.mc.infrastructure.iam.persistence.jpa;

import com.mc.infrastructure.iam.persistence.model.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA Repository for PermissionJpaEntity.
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, Long> {

    Optional<PermissionJpaEntity> findByName(String name);

    Set<PermissionJpaEntity> findByNameIn(List<String> names);

    boolean existsByName(String name);
}
