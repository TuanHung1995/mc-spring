package com.mc.controller.work.http;

import com.mc.application.work.dto.request.CreateTaskGroupRequest;
import com.mc.application.work.dto.request.UpdateTaskGroupRequest;
import com.mc.application.work.dto.response.TaskGroupResponse;
import com.mc.application.work.service.TaskGroupAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

/**
 * TaskGroupController — HTTP Adapter (Work Context)
 *
 * <p>Covers the full TaskGroup lifecycle via clean RESTful endpoints.
 * Archive/unarchive/restore/trash/permanent-delete each have dedicated endpoints
 * to keep intent explicit and avoid boolean-flag request bodies.</p>
 */
@RestController
@RequestMapping("/api/v2/task-groups")
@RequiredArgsConstructor
@Slf4j
public class TaskGroupController {

    private final TaskGroupAppService taskGroupAppService;

    @PostMapping
    @PreAuthorize("@workSecurity.canAccessBoard(#request.boardId, 'GROUP:CREATE')")
    public ResponseEntity<TaskGroupResponse> createGroup(
            @Valid @RequestBody CreateTaskGroupRequest request) {
        log.info("Creating task group in board {}", request.getBoardId());
        return ResponseEntity.ok(taskGroupAppService.createGroup(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:VIEW')")
    public ResponseEntity<TaskGroupResponse> getGroup(@PathVariable UUID id) {
        return ResponseEntity.ok(taskGroupAppService.getGroupById(id));
    }

    @GetMapping("/board/{boardId}")
    @PreAuthorize("@workSecurity.canAccessBoard(#boardId, 'GROUP:VIEW')")
    public ResponseEntity<List<TaskGroupResponse>> getGroupsByBoard(@PathVariable UUID boardId) {
        return ResponseEntity.ok(taskGroupAppService.getGroupsByBoard(boardId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:EDIT')")
    public ResponseEntity<TaskGroupResponse> updateGroup(
            @PathVariable UUID id,
            @RequestBody UpdateTaskGroupRequest request) {
        return ResponseEntity.ok(taskGroupAppService.updateGroup(id, request));
    }

    /** Soft-deletes (trashes) a task group. Recoverable via /restore. */
    @DeleteMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:TRASH')")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID id) {
        taskGroupAppService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    /** Archives a group — hidden from main board view but still accessible. */
    @PutMapping("/{id}/archive")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:ARCHIVE')")
    public ResponseEntity<Void> archiveGroup(@PathVariable UUID id) {
        taskGroupAppService.archiveGroup(id);
        return ResponseEntity.noContent().build();
    }

    /** Unarchives a previously archived group. */
    @PutMapping("/{id}/unarchive")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:ARCHIVE')")
    public ResponseEntity<Void> unarchiveGroup(@PathVariable UUID id) {
        taskGroupAppService.unarchiveGroup(id);
        return ResponseEntity.noContent().build();
    }

    /** Restores a soft-deleted (trashed) group. */
    @PutMapping("/{id}/restore")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:RESTORE')")
    public ResponseEntity<Void> restoreGroup(@PathVariable UUID id) {
        taskGroupAppService.restoreGroup(id);
        return ResponseEntity.noContent().build();
    }

    /** Permanently deletes a trashed group. Irreversible. */
    @DeleteMapping("/{id}/permanent")
    @PreAuthorize("@workSecurity.canAccessGroup(#id, 'GROUP:DELETE')")
    public ResponseEntity<Void> permanentDeleteGroup(@PathVariable UUID id) {
        taskGroupAppService.permanentDeleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/board/{boardId}/archived")
    @PreAuthorize("@workSecurity.canAccessBoard(#boardId, 'GROUP:VIEW')")
    public ResponseEntity<List<TaskGroupResponse>> getArchivedGroups(@PathVariable UUID boardId) {
        return ResponseEntity.ok(taskGroupAppService.getArchivedGroupsByBoard(boardId));
    }

    @GetMapping("/board/{boardId}/trashed")
    @PreAuthorize("@workSecurity.canAccessBoard(#boardId, 'GROUP:VIEW')")
    public ResponseEntity<List<TaskGroupResponse>> getTrashedGroups(@PathVariable UUID boardId) {
        return ResponseEntity.ok(taskGroupAppService.getTrashedGroupsByBoard(boardId));
    }
}
