package com.mc.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.model.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String resetToken;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    private String avatarUrl;
    private Date createdAt = new Date();
    private Integer failedLoginAttempts = 0;
    private String unlockToken;
    private Date updatedAt;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<TeamMember> teamMembers = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<BoardMember> boardMembers = new HashSet<>();

    public static User create(String fullName, String email, String password, AccountStatus status, AuthProvider provider) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(status);
        user.setProvider(provider);
        return user;
    }

}
