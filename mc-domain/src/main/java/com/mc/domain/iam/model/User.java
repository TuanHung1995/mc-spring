package com.mc.domain.iam.model;

import com.mc.domain.core.model.BaseDomainEntity;
import com.mc.domain.core.util.IdUtils;
import com.mc.domain.exception.DomainException;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.model.vo.UserProfile;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User Domain Entity - IAM Bounded Context
 * Rich domain model with business logic.
 */
@Getter
public class User extends BaseDomainEntity {

    private Email email;
    private String password;
    private int failedLoginAttempts = 0;
    private UserProfile profile;
    private AuthProvider provider;
    private AccountStatus status;
    private boolean emailVerified;
    private String unlockToken;

    // =================================================================
    // CONSTRUCTOR (For Persistence/Reconstitution)
    // =================================================================
    public User(UUID id, Email email, String password, int failedLoginAttempts, UserProfile profile,
                AuthProvider provider, AccountStatus status, boolean emailVerified, String unlockToken,
                LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        super(id, createdAt, updatedAt, deleted);
        this.email = email;
        this.password = password;
        this.failedLoginAttempts = failedLoginAttempts;
        this.profile = profile;
        this.provider = provider;
        this.status = status;
        this.emailVerified = emailVerified;
        this.unlockToken = unlockToken;
    }

    private User() {
        super();
    }

    // =================================================================
    // FACTORY METHODS (Creation)
    // =================================================================
    
    /**
     * Register a new user with email and password.
     * Uses IdUtils for ID generation.
     */
    public static User registerWithEmail(Email email, String passwordHash, String fullName) {
        User user = new User();
        user.initializeNewEntity(IdUtils.newId());
        user.email = email;
        user.password = passwordHash;
        user.profile = new UserProfile(fullName, null);
        user.provider = AuthProvider.LOCAL;
        user.status = AccountStatus.ACTIVE;
        user.emailVerified = false;
        return user;
    }

    /**
     * Register with OAuth provider.
     */
    public static User registerWithOAuth(Email email, String fullName, String avatarUrl, AuthProvider provider) {
        User user = new User();
        user.initializeNewEntity(IdUtils.newId());
        user.email = email;
        user.password = null; // OAuth users don't have password
        user.profile = new UserProfile(fullName, avatarUrl);
        user.provider = provider;
        user.status = AccountStatus.ACTIVE;
        user.emailVerified = true; // OAuth emails are pre-verified
        return user;
    }

    // =================================================================
    // BUSINESS LOGIC (Behavior)
    // =================================================================
    
    public void updateProfile(String fullName, String avatarUrl, String bio) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new DomainException("Inactive user cannot update profile");
        }
        this.profile = this.profile.update(fullName, avatarUrl);
        markAsModified();
    }

    public void changePassword(String newPasswordHash) {
        if (this.provider != AuthProvider.LOCAL) {
            throw new DomainException("Cannot change password for social account");
        }
        this.password = newPasswordHash;
        markAsModified();
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts += 1;
        markAsModified();
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        markAsModified();
    }

    public void generateUnlockToken(String token) {
        this.unlockToken = token;
        markAsModified();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        markAsModified();
    }

    public void deactivate() {
        this.status = AccountStatus.LOCKED;
        markAsModified();
    }

    public void activate() {
        this.status = AccountStatus.ACTIVE;
        markAsModified();
    }

    public String getEmailValue() {
        return this.email.getValue();
    }

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }
}