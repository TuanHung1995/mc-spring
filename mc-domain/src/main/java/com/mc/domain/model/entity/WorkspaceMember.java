package com.mc.domain.model.entity;

import com.mc.domain.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "workspace_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMember {

    private RoleType role;
    private Date joinedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private Workspace workspace;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
