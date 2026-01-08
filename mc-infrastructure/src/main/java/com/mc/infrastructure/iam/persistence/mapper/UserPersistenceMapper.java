package com.mc.infrastructure.iam.persistence.mapper;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.model.vo.UserProfile;
import com.mc.infrastructure.iam.persistence.model.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    // Domain -> Infra (Để lưu vào DB)
    public UserJpaEntity toEntity(User domain) {
        if (domain == null) return null;

        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail().getValue()); // Extract value
        entity.setPassword(domain.getPassword());

        // Flatten UserProfile VO
        if (domain.getProfile() != null) {
            entity.setFullName(domain.getProfile().getFullName());
            entity.setAvatarUrl(domain.getProfile().getAvatarUrl());
        }

        entity.setProvider(domain.getProvider());
        entity.setStatus(domain.getStatus());
        entity.setEmailVerified(domain.isEmailVerified());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    // Infra -> Domain (Để xử lý nghiệp vụ)
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                new Email(entity.getEmail()), // Reconstruct VO
                entity.getPassword(),
                new UserProfile(entity.getFullName(), entity.getAvatarUrl()), // Reconstruct VO
                entity.getProvider(),
                entity.getStatus(),
                entity.isEmailVerified(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isDeleted()
        );
    }
}
