package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.ColumnValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnValueJPAMapper extends JpaRepository<ColumnValue, Long> {
}
