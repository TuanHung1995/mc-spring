package com.mc.application.work.service.impl;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.*;
import com.mc.application.work.service.TaskGroupAppService;
import com.mc.domain.core.event.BoardChangedEvent;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.work.port.WorkUserContextPort;
import com.mc.domain.work.model.entity.TaskGroup;
import com.mc.domain.work.repository.TaskGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TaskGroupAppServiceImpl — Application Service (Work Context)
 *
 * <p>Manages the full TaskGroup lifecycle: create, update, archive, unarchive,
 * trash (soft-delete), restore, permanent-delete.</p>
 */
@Service
@RequiredArgsConstructor
public class TaskGroupAppServiceImpl implements TaskGroupAppService {

    private final TaskGroupRepository taskGroupRepository;
    private final WorkUserContextPort workUserContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public TaskGroupResponse createGroup(CreateTaskGroupRequest request) {
        UUID userId = workUserContextPort.getCurrentUser().id();

        Double maxPos = taskGroupRepository.getMaxPositionByBoardId(request.getBoardId());
        double newPos = (maxPos != null) ? maxPos + 65536 : 65536;

        TaskGroup group = TaskGroup.create(request.getBoardId(), request.getWorkspaceId(), request.getTeamId(),
                request.getTitle(), request.getColor(), newPos, userId);
        TaskGroup saved = taskGroupRepository.save(group);

//        publishEvent("GROUP_CREATED", saved.getId(), saved.getBoardId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskGroupResponse getGroupById(UUID groupId) {
        return toResponse(requireGroup(groupId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroupResponse> getGroupsByBoard(UUID boardId) {
        return taskGroupRepository.findActiveByBoardId(boardId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskGroupResponse updateGroup(UUID groupId, UpdateTaskGroupRequest request) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        TaskGroup group = requireGroup(groupId);
        group.update(request.getTitle(), request.getColor(), userId);
        TaskGroup saved = taskGroupRepository.save(group);
//        publishEvent("GROUP_UPDATED", saved.getId(), saved.getBoardId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteGroup(UUID groupId) {
        UUID userId = workUserContextPort.getCurrentUser().id();
        TaskGroup group = requireGroup(groupId);
        UUID boardId = group.getBoardId();
        group.trash(userId);
        taskGroupRepository.delete(group);
//        publishEvent("GROUP_DELETED", groupId, boardId);
    }

    @Override
    @Transactional
    public void archiveGroup(UUID groupId) {
        UUID userId = workUserContextPort.getCurrentUser().id();
        TaskGroup group = requireGroup(groupId);
        group.archive(userId);
        TaskGroup saved = taskGroupRepository.save(group);
//        publishEvent("GROUP_ARCHIVED", saved.getId(), saved.getBoardId());
    }

    @Override
    @Transactional
    public void unarchiveGroup(UUID groupId) {
        TaskGroup group = requireGroup(groupId);
        group.unarchive();
        TaskGroup saved = taskGroupRepository.save(group);
//        publishEvent("GROUP_UNARCHIVED", saved.getId(), saved.getBoardId());
    }

    @Override
    @Transactional
    public void restoreGroup(UUID groupId) {
        TaskGroup group = taskGroupRepository.findByIdIncludingDeleted(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        group.restore();
        TaskGroup saved = taskGroupRepository.save(group);
//        publishEvent("GROUP_RESTORED", saved.getId(), saved.getBoardId());
    }

    @Override
    @Transactional
    public void permanentDeleteGroup(UUID groupId) {
        TaskGroup group = taskGroupRepository.findByIdIncludingDeleted(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
        UUID boardId = group.getBoardId();
        taskGroupRepository.permanentDelete(group);
//        publishEvent("GROUP_PERMANENTLY_DELETED", groupId, boardId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroupResponse> getArchivedGroupsByBoard(UUID boardId) {
        return taskGroupRepository.findArchivedByBoardId(boardId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskGroupResponse> getTrashedGroupsByBoard(UUID boardId) {
        return taskGroupRepository.findTrashedByBoardId(boardId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // =================================================================
    // HELPERS
    // =================================================================

    private TaskGroup requireGroup(UUID groupId) {
        return taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", groupId));
    }

    private void publishEvent(String type, UUID entityId, UUID boardId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        payload.put("entityId", entityId);
        payload.put("boardId", boardId);
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    private TaskGroupResponse toResponse(TaskGroup g) {
        return new TaskGroupResponse(g.getId(), g.getBoardId(), g.getTitle(), g.getColor(),
                g.getPosition(), g.isCollapsed(), g.isArchived(), g.getArchivedAt(),
                g.getCreatedAt(), g.getUpdatedAt());
    }
}
