package com.mc.domain.model.entity;

import com.mc.domain.model.enums.BoardColumnType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "board_columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Enumerated(EnumType.STRING)
    private BoardColumnType type;
    private Set<String> settings;
    private String description;
    private double position;
    private int width;
    private boolean isHidden;
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @OneToMany(mappedBy = "column")
    private Set<ColumnValue> columnValues;

}
