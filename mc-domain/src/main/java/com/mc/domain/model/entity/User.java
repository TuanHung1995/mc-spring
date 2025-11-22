package com.mc.domain.model.entity;

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
    //    private boolean isActive;
    private String fullName;
    private String email;
    private String phone;
    private String password; // Password hash
    private String status; // ACTIVE, INACTIVE, SUSPENDED
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    private User(String fullName, String email, String password, String status) {
        this.status = status;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    // Factory
    public static User create(String fullName, String email, String password, String status) {
        return new User(fullName, email, password, status);
    }

    // assign role to user within a team
    public void assignRole(Role role, Team team) {
        UserRole userRole = new UserRole();
        userRole.setUser(this);
        userRole.setRole(role);
        userRole.setTeam(team);

        this.userRoles.add(userRole);
    }

}
