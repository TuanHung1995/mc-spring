package com.mc.controller.work.http;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.BoardResponse;
import com.mc.application.work.service.BoardAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardController — HTTP Adapter (Work Context)
 *
 * <p>Exposes Board CRUD, inline-edit, drag-and-drop reorder, and invite endpoints.
 * All {@code @PreAuthorize} checks are kept at the controller boundary (Spring Security best practice).
 * User identity is resolved internally via {@link com.mc.domain.port.UserContextPort}.</p>
 */
@RestController
@RequestMapping("/api/v2/boards")
@Component("workBoardController")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    @Qualifier("workBoardAppService")
    private final BoardAppService boardAppService;

    // =================================================================
    // BOARD CRUD
    // =================================================================

    @PostMapping
//    @PreAuthorize("hasPermission(#request.workspaceId, 'Workspace', 'BOARD:CREATE')")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        log.info("Creating board in workspace {}", request.getWorkspaceId());
        return ResponseEntity.ok(boardAppService.createBoard(request));
    }

    @GetMapping("/{boardId}")
//    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:VIEW')")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardAppService.getBoardById(boardId));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getMyBoards() {
        return ResponseEntity.ok(boardAppService.getBoardsForCurrentUser());
    }

    /**
     * Soft-trashes (moves to Trash bin) a board.
     * POST kept instead of DELETE for semantic clarity — trash is reversible.
     */
    @PostMapping("/{boardId}/trash")
//    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:TRASH')")
    public ResponseEntity<Void> trashBoard(@PathVariable Long boardId) {
        boardAppService.trashBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Permanently removes a board and all its data. Irreversible.
     */
    @DeleteMapping("/{boardId}/permanent")
//    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:DELETE')")
    public ResponseEntity<Void> deleteBoardPermanently(@PathVariable Long boardId) {
        boardAppService.deleteBoardPermanently(boardId);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // INLINE ELEMENT EDIT
    // =================================================================

    /**
     * Inline-edit a single field on any board element (group title, column title, item name).
     * Uses a discriminator type field ("TASK_GROUP" | "COLUMN" | "ITEM").
     */
    @PutMapping("/{boardId}/elements")
//    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:EDIT')")
    public ResponseEntity<Void> updateBoardElement(
            @PathVariable Long boardId,
            @RequestBody UpdateBoardElementRequest request) {
        boardAppService.updateBoardElement(request);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // DRAG-AND-DROP REORDER
    // =================================================================

    /**
     * Reorders task groups within a board (drag-and-drop).
     */
    @PutMapping("/groups/reorder")
//    @PreAuthorize("hasPermission(#request.targetId, 'Group', 'BOARD:EDIT')")
    public ResponseEntity<Void> reorderGroup(@RequestBody ReorderRequest request) {
        boardAppService.reorderGroup(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reorders columns within a board (drag-and-drop).
     */
    @PutMapping("/columns/reorder")
//    @PreAuthorize("hasPermission(#request.targetId, 'Column', 'BOARD:EDIT')")
    public ResponseEntity<Void> reorderColumn(@RequestBody ReorderRequest request) {
        boardAppService.reorderColumn(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reorders items within or across task groups (drag-and-drop).
     */
    @PutMapping("/items/reorder")
//    @PreAuthorize("hasPermission(#request.targetId, 'Item', 'BOARD:EDIT')")
    public ResponseEntity<Void> reorderItem(@RequestBody ReorderRequest request) {
        boardAppService.reorderItem(request);
        return ResponseEntity.noContent().build();
    }
}
