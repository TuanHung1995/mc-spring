package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.infrastructure.persistence.mapper.TaskGroupJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public TaskGroup save(TaskGroup group) {
        return taskGroupJPAMapper.save(group);
    }

    @Override
    public void delete(TaskGroup group) {
        if (group.getDeletedAt() != null || group.getDeletedBy() != null) {
            taskGroupJPAMapper.saveAndFlush(group);
        }
        taskGroupJPAMapper.delete(group);
    }

    @Override
    public List<TaskGroup> findByBoardId(Long boardId) {
        return taskGroupJPAMapper.findByBoardId(boardId);
    }

    @Override
    public List<TaskGroup> findArchivedGroupsByBoardId(Long boardId) {
        return taskGroupJPAMapper.findArchivedGroupsByBoardId(boardId);
    }

    @Override
    public Double getPosition(Long groupId) {
        return taskGroupJPAMapper.getPosition(groupId);
    }

    @Override
    public Double getMaxPositionByBoardId(Long boardId) {
        return taskGroupJPAMapper.getMaxPositionByBoardId(boardId);
    }

    @Override
    public Optional<Long> findBoardIdByGroupId(Long groupId) {
        return taskGroupJPAMapper.findBoardIdByGroupId(groupId);
    }

}
