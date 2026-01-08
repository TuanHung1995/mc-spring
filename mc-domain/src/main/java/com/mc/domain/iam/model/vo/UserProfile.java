package com.mc.domain.iam.model.vo;

import lombok.*;

/**
 * UserProfile Value Object - Pure Domain (no JPA dependencies)
 * Immutable value object grouping user profile details.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfile {

    private final String fullName;
    private final String avatarUrl;

    /**
     * Business method to update profile partially.
     * Returns a NEW UserProfile (Immutability).
     */
    public UserProfile update(String newName, String newAvatar) {
        return new UserProfile(
                newName != null ? newName : this.fullName,
                newAvatar != null ? newAvatar : this.avatarUrl
        );
    }
}

