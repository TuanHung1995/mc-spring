package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.infrastructure.persistence.mapper.TaskGroupJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskGroupInfrasRepositoryImpl implements TaskGroupRepository {

    private final TaskGroupJPAMapper taskGroupJPAMapper;

    @Override
    public Optional<TaskGroup> findById(Long id) {
        return taskGroupJPAMapper.findById(id);
    }

    @Override
    public void save(TaskGroup group) {
        taskGroupJPAMapper.save(group);
    }


    @Override
    public Double getPosition(Long groupId) {
        return taskGroupJPAMapper.getPosition(groupId);
    }

}
