package com.mc.domain.repository;

import com.mc.domain.model.entity.TaskGroup;

import java.util.Optional;

public interface TaskGroupRepository {

    Optional<TaskGroup> findById(Long id);
    void save(TaskGroup group);
    Double getPosition(Long groupId);

}
