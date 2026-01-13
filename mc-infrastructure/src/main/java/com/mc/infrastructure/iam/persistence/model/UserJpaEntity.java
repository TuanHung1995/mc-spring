package com.mc.infrastructure.iam.persistence.model;

import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import com.mc.infrastructure.persistence.model.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "unlock_token")
    private String unlockToken;

}
