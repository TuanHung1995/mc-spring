package com.mc.domain.model.entity;

import com.mc.domain.model.enums.BoardColumnType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "board_columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
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
    private Date deletedAt;

    @jakarta.persistence.Column(name = "is_deleted", insertable = false, updatable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    private User deletedBy;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ColumnValue> columnValues;

}
