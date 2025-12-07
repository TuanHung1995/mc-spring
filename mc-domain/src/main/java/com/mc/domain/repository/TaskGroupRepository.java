package com.mc.domain.repository;

import com.mc.domain.model.entity.TaskGroup;

import java.util.Optional;

public interface TaskGroupRepository {

    Optional<TaskGroup> findById(Long id);
    TaskGroup save(TaskGroup group);
    Double getPosition(Long groupId);

    Optional<Long> findBoardIdByGroupId(Long groupId);

}
