package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJPAMapper extends JpaRepository<Board, Long> {



}
