package com.mc.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "column_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    private String textValue;
    private String color;
    private String type;
    private Date createdAt = new Date();
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "column_id", referencedColumnName = "id")
    private Column column;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

}
