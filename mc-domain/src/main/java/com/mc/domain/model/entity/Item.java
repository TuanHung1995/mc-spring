package com.mc.domain.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double position;
    private Date createdAt = new Date();
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private TaskGroup group;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    private User deletedBy;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @OneToMany(mappedBy = "item")
    private Set<ColumnValue> columnValues;

}
