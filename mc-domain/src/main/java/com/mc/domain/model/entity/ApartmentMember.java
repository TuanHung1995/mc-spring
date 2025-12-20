package com.mc.domain.model.entity;

import com.mc.domain.model.enums.ApartmentMemberStatus;
import com.mc.domain.model.enums.RoleType;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "apartment_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOwner;

    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
    private ApartmentMemberStatus status;

    private Date joinedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

}
