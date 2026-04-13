package com.mc.controller.work.http;

import com.mc.application.work.dto.request.CreateItemRequest;
import com.mc.application.work.dto.response.ItemResponse;
import com.mc.application.work.service.ItemAppService;
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
 * ItemController — HTTP Adapter (Work Context)
 *
 * <p>Exposes Item CRUD. Item reorder (drag-and-drop) is handled by {@link BoardController}
 * under {@code PUT /api/v1/boards/items/reorder}.</p>
 */
@RestController
@RequestMapping("/api/v2/items")
@Component("workItemController")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    @Qualifier("workItemAppService")
    private final ItemAppService itemAppService;

    @PostMapping
//    @PreAuthorize("hasPermission(#request.boardId, 'Board', 'ITEM:CREATE')")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        log.info("Creating item in board {} group {}", request.getBoardId(), request.getGroupId());
        return ResponseEntity.ok(itemAppService.createItem(request));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:VIEW')")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemAppService.getItemById(id));
    }

    @GetMapping("/group/{groupId}")
//    @PreAuthorize("hasPermission(#groupId, 'Group', 'ITEM:VIEW')")
    public ResponseEntity<List<ItemResponse>> getItemsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(itemAppService.getItemsByGroup(groupId));
    }

    @GetMapping("/board/{boardId}")
//    @PreAuthorize("hasPermission(#boardId, 'Board', 'ITEM:VIEW')")
    public ResponseEntity<List<ItemResponse>> getItemsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(itemAppService.getItemsByBoard(boardId));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:EDIT')")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long id,
            @RequestParam String name) {
        return ResponseEntity.ok(itemAppService.updateItemName(id, name));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:DELETE')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemAppService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
