package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.repository.TaskGroupRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.TaskGroupDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskGroupDomainServiceImpl implements TaskGroupDomainService {

    private final TaskGroupRepository taskGroupRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PositionCalculationService positionCalculationService;

    @Override
    @Transactional
    public TaskGroup createGroup(Long boardId, String title, String color, Long userId) {
        // Validate board exists
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        // Get creator
        User creator = userRepository.findById(userId)
                .orElse(null);
        
        // Calculate position (append to end)
        Double maxPosition = taskGroupRepository.getMaxPositionByBoardId(boardId);
        double newPosition = (maxPosition != null) ? maxPosition + 65536 : 65536;
        
        // Create group
        TaskGroup group = new TaskGroup();
        group.setTitle(title);
        group.setColor(color != null ? color : "#579bfc"); // Default color
        group.setBoard(board);
        group.setCreatedBy(creator);
        group.setPosition(newPosition);
        group.setCollapsed(false);
        group.setCreatedAt(new Date());
        
        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskGroup getGroupById(Long groupId) {
        return taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroup> getGroupsByBoardId(Long boardId) {
        // Validate board exists
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        return taskGroupRepository.findByBoardId(boardId);
    }

    @Override
    @Transactional
    public TaskGroup updateTaskGroup(Long groupId, String newTitle, String newColor) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));

        if (newTitle == null && newColor == null) return group;

        if (newTitle != null) group.setTitle(newTitle);
        if (newColor != null) group.setColor(newColor);

        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional
    public TaskGroup reorderGroup(Long targetGroupId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? taskGroupRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? taskGroupRepository.getPosition(nextItemId) : null;

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Group
        TaskGroup group = taskGroupRepository.findById(targetGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", targetGroupId));

        // 4. Cập nhật vị trí
        group.setPosition(newPos);
        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        
        // Get user who is deleting
        User deleter = userRepository.findById(userId)
                .orElse(null);
        
        // Set soft delete fields (if entity has these fields, otherwise just delete)
        group.setDeletedBy(deleter);
        group.setDeletedAt(new Date());
        
        // Soft delete
        taskGroupRepository.delete(group);
    }

    @Override
    @Transactional
    public TaskGroup archiveGroup(Long groupId, Long userId) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));

        User currentUser = userRepository.findById(userId)
                .orElse(null);

        group.setArchived(true);
        group.setArchivedAt(new Date());
        group.setArchivedBy(currentUser);

        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional
    public TaskGroup unarchiveGroup(Long groupId, Long userId) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));

        User currentUser = userRepository.findById(userId)
                .orElse(null);

        // Remove archived flag
        group.setArchived(false);
        group.setArchivedAt(null);
        group.setArchivedBy(null);
        
        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroup> getArchivedGroupsByBoardId(Long boardId) {
        // Validate board exists
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        return taskGroupRepository.findArchivedGroupsByBoardId(boardId);
    }

    @Override
    @Transactional
    public TaskGroup restoreGroup(Long groupId, Long userId) {
        // Find group including deleted ones
        System.out.println("Restoring group with ID: " + groupId);
        TaskGroup group = taskGroupRepository.findByIdIncludingDeleted(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        
        // Clear soft delete fields
        group.setDeletedAt(null);
        group.setDeletedBy(null);
        
        return taskGroupRepository.save(group);
    }

    @Override
    @Transactional
    public Long permanentDeleteGroup(Long groupId) {
        // Find group including deleted ones
        TaskGroup group = taskGroupRepository.findByIdIncludingDeleted(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        
        // Get boardId before deletion
        Long boardId = group.getBoard() != null ? group.getBoard().getId() : null;
        
        // Permanently delete from database
        taskGroupRepository.permanentDelete(group);
        
        return boardId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroup> getTrashedGroupsByBoardId(Long boardId) {
        // Validate board exists
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        return taskGroupRepository.findTrashedGroupsByBoardId(boardId);
    }

}
