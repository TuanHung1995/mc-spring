package com.mc.infrastructure.iam.persistence.model;

import com.mc.domain.iam.model.enums.AccountStatus;
import com.mc.domain.iam.model.enums.AuthProvider;
import com.mc.infrastructure.core.persistence.model.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "iam_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job_title")
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "unlock_token")
    private String unlockToken;

}
