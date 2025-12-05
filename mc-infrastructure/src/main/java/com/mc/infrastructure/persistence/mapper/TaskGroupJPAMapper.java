package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskGroupJPAMapper extends JpaRepository<TaskGroup, Long> {

    @Query("SELECT tg.position FROM TaskGroup tg WHERE tg.id = :groupId")
    Double getPosition(Long groupId);

}
