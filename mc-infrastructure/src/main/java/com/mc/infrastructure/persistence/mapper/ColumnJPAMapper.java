package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ColumnJPAMapper extends JpaRepository<Column, Long> {

    @Query("SELECT c.position FROM Column c WHERE c.id = :columnId")
    double getPosition(Long columnId);

}
