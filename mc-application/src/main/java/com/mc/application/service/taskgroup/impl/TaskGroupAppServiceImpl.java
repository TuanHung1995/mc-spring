package com.mc.application.service.taskgroup.impl;

import com.mc.application.mapper.TaskGroupMapper;
import com.mc.application.model.taskgroup.*;
import com.mc.application.service.taskgroup.TaskGroupAppService;
import com.mc.domain.event.BoardChangedEvent;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.TaskGroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskGroupAppServiceImpl implements TaskGroupAppService {

    private final TaskGroupDomainService taskGroupDomainService;
    private final TaskGroupMapper taskGroupMapper;
    private final UserContextPort userContextPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CreateTaskGroupResponse createGroup(CreateTaskGroupRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        TaskGroup group = taskGroupDomainService.createGroup(
                request.getBoardId(),
                request.getTitle(),
                request.getColor(),
                currentUserId
        );

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_CREATED");
        payload.put("entityId", group.getId());
        payload.put("boardId", group.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(group.getBoard().getId(), payload));

        return taskGroupMapper.toCreateTaskGroupResponse(group);
    }

    @Override
    public GetTaskGroupResponse getGroup(Long groupId) {
        TaskGroup group = taskGroupDomainService.getGroupById(groupId);
        return taskGroupMapper.toGetTaskGroupResponse(group);
    }

    @Override
    public List<GetTaskGroupResponse> getGroupsByBoard(Long boardId) {
        List<TaskGroup> groups = taskGroupDomainService.getGroupsByBoardId(boardId);
        return groups.stream()
                .map(taskGroupMapper::toGetTaskGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UpdateTaskGroupResponse updateGroup(UpdateTaskGroupRequest request) {
        TaskGroup group = taskGroupDomainService.updateTaskGroup(
                request.getGroupId(),
                request.getTitle(),
                request.getColor()
        );

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_UPDATED");
        payload.put("entityId", group.getId());
        payload.put("boardId", group.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(group.getBoard().getId(), payload));

        return taskGroupMapper.toUpdateTaskGroupResponse(group);
    }

    @Override
    public void deleteGroup(DeleteTaskGroupRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        // Get group before deletion for event
        TaskGroup group = taskGroupDomainService.getGroupById(request.getGroupId());
        Long boardId = group.getBoard().getId();
        
        taskGroupDomainService.deleteGroup(request.getGroupId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_DELETED");
        payload.put("entityId", request.getGroupId());
        payload.put("boardId", boardId);
        
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    @Override
    public void archiveGroup(ArchiveTaskGroupRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        TaskGroup group = taskGroupDomainService.archiveGroup(request.getGroupId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_ARCHIVED");
        payload.put("entityId", group.getId());
        payload.put("boardId", group.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(group.getBoard().getId(), payload));
    }

    @Override
    public void unarchiveGroup(UnarchiveTaskGroupRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        TaskGroup group = taskGroupDomainService.unarchiveGroup(request.getGroupId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_UNARCHIVED");
        payload.put("entityId", group.getId());
        payload.put("boardId", group.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(group.getBoard().getId(), payload));
    }

    @Override
    public List<GetTaskGroupResponse> getArchivedGroupsByBoard(Long boardId) {
        List<TaskGroup> groups = taskGroupDomainService.getArchivedGroupsByBoardId(boardId);
        return groups.stream()
                .map(taskGroupMapper::toGetTaskGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void restoreGroup(RestoreTaskGroupRequest request) {
        Long currentUserId = userContextPort.getCurrentUserId();
        
        TaskGroup group = taskGroupDomainService.restoreGroup(request.getGroupId(), currentUserId);

        // Publish event for real-time updates
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_RESTORED");
        payload.put("entityId", group.getId());
        payload.put("boardId", group.getBoard().getId());
        
        eventPublisher.publishEvent(new BoardChangedEvent(group.getBoard().getId(), payload));
    }

    @Override
    public void permanentDeleteGroup(PermanentDeleteTaskGroupRequest request) {
        // Domain service returns boardId before deletion
        Long boardId = taskGroupDomainService.permanentDeleteGroup(request.getGroupId());

        // Publish event for real-time updates
        if (boardId != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "GROUP_PERMANENTLY_DELETED");
            payload.put("entityId", request.getGroupId());
            payload.put("boardId", boardId);
            
            eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
        }
    }

    @Override
    public List<GetTaskGroupResponse> getTrashedGroupsByBoard(Long boardId) {
        List<TaskGroup> groups = taskGroupDomainService.getTrashedGroupsByBoardId(boardId);
        return groups.stream()
                .map(taskGroupMapper::toGetTaskGroupResponse)
                .collect(Collectors.toList());
    }
}
