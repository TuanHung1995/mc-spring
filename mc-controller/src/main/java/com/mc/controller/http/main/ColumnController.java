package com.mc.controller.http.main;

import com.mc.application.model.column.*;
import com.mc.application.service.column.ColumnAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
@RequiredArgsConstructor
@Slf4j
public class ColumnController {

    private final ColumnAppService columnAppService;

    /**
     * Create a new column
     *
     * @param request the create column request
     * @return created column response
     */
    @PostMapping
    @PreAuthorize("hasPermission(#request.boardId, 'Board', 'COLUMN:CREATE')")
    public ResponseEntity<CreateColumnResponse> createColumn(@Valid @RequestBody CreateColumnRequest request) {
        log.info("Creating column in board {}", request.getBoardId());
        return ResponseEntity.ok(columnAppService.createColumn(request));
    }

    /**
     * Get column by ID
     *
     * @param id the ID of the column
     * @return column details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Column', 'COLUMN:VIEW')")
    public ResponseEntity<GetColumnResponse> getColumn(@PathVariable Long id) {
        return ResponseEntity.ok(columnAppService.getColumn(id));
    }

    /**
     * Update a column
     *
     * @param id the ID of the column
     * @param request the update request
     * @return updated column response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Column', 'COLUMN:EDIT')")
    public ResponseEntity<UpdateColumnResponse> updateColumn(
            @PathVariable Long id,
            @Valid @RequestBody UpdateColumnRequest request) {
        request.setColumnId(id);
        return ResponseEntity.ok(columnAppService.updateColumn(request));
    }

    /**
     * Delete (soft delete) a column
     *
     * @param id the ID of the column
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Column', 'COLUMN:DELETE')")
    public ResponseEntity<String> deleteColumn(@PathVariable Long id) {
        DeleteColumnRequest request = new DeleteColumnRequest();
        request.setColumnId(id);
        columnAppService.deleteColumn(request);
        return ResponseEntity.ok("Column deleted successfully");
    }

    /**
     * Get all columns in a board
     *
     * @param boardId the ID of the board
     * @return list of columns
     */
    @GetMapping("/board/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'Board', 'COLUMN:VIEW')")
    public ResponseEntity<List<GetColumnResponse>> getColumnsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(columnAppService.getColumnsByBoard(boardId));
    }

}
