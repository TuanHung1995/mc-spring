package com.mc.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.NumericBooleanConverter;

import java.util.Date;

@Entity
@Table(name = "task_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "is_deleted", converter = NumericBooleanConverter.class)
@FilterDef(
        name = "deletedFilter",
        parameters = @ParamDef(name = "deleted", type = Boolean.class)
)
@Filter(name = "deletedFilter", condition = "is_deleted = :deleted")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String color;
    private double position;
    private boolean isCollapsed;
    private boolean isArchived = false;
    
    private Date createdAt = new Date();
    private Date updatedAt;
    private Date deletedAt;
    private Date archivedAt;

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

    @ManyToOne
    @JoinColumn(name = "archived_by", referencedColumnName = "id")
    private User archivedBy;

}
