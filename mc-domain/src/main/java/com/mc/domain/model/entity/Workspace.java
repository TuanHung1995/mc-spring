package com.mc.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status = "ACTIVE";
    private Date createdAt = new Date();
    private Date updatedAt;
    private Date deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "workspace_members",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;


    public static Workspace create(String name, User createdBy, Team team) {
        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setCreatedBy(createdBy);
        workspace.setTeam(team);
        return workspace;
    }

}
