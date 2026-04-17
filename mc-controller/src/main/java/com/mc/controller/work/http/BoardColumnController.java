package com.mc.controller.work.http;

import com.mc.application.work.dto.request.CreateColumnRequest;
import com.mc.application.work.dto.response.ColumnResponse;
import com.mc.application.work.service.BoardColumnAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardColumnController — HTTP Adapter (Work Context)
 *
 * <p>Exposes Board Column CRUD. Column reorder is handled by {@link BoardController}
 * under {@code PUT /api/v1/boards/columns/reorder} since it publishes a board-level event.</p>
 */
@RestController
@RequestMapping("/api/v2/columns")
@Component("workBoardColumnController")
@RequiredArgsConstructor
@Slf4j
public class BoardColumnController {

    @Qualifier("workBoardColumnAppService")
    private final BoardColumnAppService boardColumnAppService;

    @PostMapping
    @PreAuthorize("@workSecurity.canAccessBoard(#request.boardId, 'COLUMN:CREATE')")
    public ResponseEntity<ColumnResponse> createColumn(
            @Valid @RequestBody CreateColumnRequest request) {
        log.info("Creating column in board {}", request.getBoardId());
        return ResponseEntity.ok(boardColumnAppService.createColumn(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessColumn(#id, 'COLUMN:VIEW')")
    public ResponseEntity<ColumnResponse> getColumn(@PathVariable Long id) {
        return ResponseEntity.ok(boardColumnAppService.getColumnById(id));
    }

    @GetMapping("/board/{boardId}")
    @PreAuthorize("@workSecurity.canAccessBoard(#boardId, 'COLUMN:VIEW')")
    public ResponseEntity<List<ColumnResponse>> getColumnsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardColumnAppService.getColumnsByBoard(boardId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessColumn(#id, 'COLUMN:EDIT')")
    public ResponseEntity<ColumnResponse> updateColumn(
            @PathVariable Long id,
            @RequestParam String title) {
        return ResponseEntity.ok(boardColumnAppService.updateColumnTitle(id, title));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessColumn(#id, 'COLUMN:DELETE')")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long id) {
        boardColumnAppService.deleteColumn(id);
        return ResponseEntity.noContent().build();
    }
}
