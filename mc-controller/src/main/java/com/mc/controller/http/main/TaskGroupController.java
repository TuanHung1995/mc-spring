package com.mc.controller.http.main;

import com.mc.application.model.taskgroup.*;
import com.mc.application.service.taskgroup.TaskGroupAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task-groups")
@RequiredArgsConstructor
@Slf4j
public class TaskGroupController {

    private final TaskGroupAppService taskGroupAppService;

    /**
     * Create a new task group
     *
     * @param request the create group request
     * @return created group response
     */
    @PostMapping
    @PreAuthorize("hasPermission(#request.boardId, 'Board', 'GROUP:CREATE')")
    public ResponseEntity<CreateTaskGroupResponse> createGroup(@Valid @RequestBody CreateTaskGroupRequest request) {
        log.info("Creating task group in board {}", request.getBoardId());
        return ResponseEntity.ok(taskGroupAppService.createGroup(request));
    }

    /**
     * Get task group by ID
     *
     * @param id the ID of the group
     * @return group details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Group', 'GROUP:VIEW')")
    public ResponseEntity<GetTaskGroupResponse> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(taskGroupAppService.getGroup(id));
    }

    /**
     * Update a task group
     *
     * @param id the ID of the group
     * @param request the update request
     * @return updated group response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Group', 'GROUP:EDIT')")
    public ResponseEntity<UpdateTaskGroupResponse> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskGroupRequest request) {
        request.setGroupId(id);
        return ResponseEntity.ok(taskGroupAppService.updateGroup(request));
    }

    /**
     * Delete (soft delete) a task group
     *
     * @param id the ID of the group
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Group', 'GROUP:DELETE')")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
        DeleteTaskGroupRequest request = new DeleteTaskGroupRequest();
        request.setGroupId(id);
        taskGroupAppService.deleteGroup(request);
        return ResponseEntity.ok("Task group deleted successfully");
    }

    /**
     * Get all task groups in a board
     *
     * @param boardId the ID of the board
     * @return list of groups
     */
    @GetMapping("/board/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'Board', 'GROUP:VIEW')")
    public ResponseEntity<List<GetTaskGroupResponse>> getGroupsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(taskGroupAppService.getGroupsByBoard(boardId));
    }

    /**
     * Archive a task group
     *
     * @param id the ID of the group
     * @return success message
     */
    @PutMapping("/{id}/archive")
    @PreAuthorize("hasPermission(#id, 'Group', 'GROUP:ARCHIVE')")
    public ResponseEntity<String> archiveGroup(@PathVariable Long id) {
        ArchiveTaskGroupRequest request = new ArchiveTaskGroupRequest();
        request.setGroupId(id);
        taskGroupAppService.archiveGroup(request);
        return ResponseEntity.ok("Task group archived successfully");
    }

    /**
     * Unarchive a task group
     *
     * @param id the ID of the group
     * @return success message
     */
    @PutMapping("/{id}/unarchive")
    @PreAuthorize("hasPermission(#id, 'Group', 'GROUP:ARCHIVE')")
    public ResponseEntity<String> unarchiveGroup(@PathVariable Long id) {
        UnarchiveTaskGroupRequest request = new UnarchiveTaskGroupRequest();
        request.setGroupId(id);
        taskGroupAppService.unarchiveGroup(request);
        return ResponseEntity.ok("Task group unarchived successfully");
    }

    /**
     * Get all archived task groups in a board
     *
     * @param boardId the ID of the board
     * @return list of archived groups
     */
    @GetMapping("/board/{boardId}/archived")
    @PreAuthorize("hasPermission(#boardId, 'Board', 'GROUP:VIEW')")
    public ResponseEntity<List<GetTaskGroupResponse>> getArchivedGroupsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(taskGroupAppService.getArchivedGroupsByBoard(boardId));
    }

}
