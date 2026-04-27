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
import java.util.UUID;

/**
 * ItemController — HTTP Adapter (Work Context)
 *
 * <p>Exposes Item CRUD. Item reorder (drag-and-drop) is handled by {@link BoardController}
 * under {@code PUT /api/v1/boards/items/reorder}.</p>
 */
@RestController
@RequestMapping("/api/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemAppService itemAppService;

    @PostMapping
    @PreAuthorize("@workSecurity.canAccessBoard(#request.boardId, 'ITEM:CREATE')")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        log.info("Creating item in board {} group {}", request.getBoardId(), request.getGroupId());
        return ResponseEntity.ok(itemAppService.createItem(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessItem(#id, 'ITEM:VIEW')")
    public ResponseEntity<ItemResponse> getItem(@PathVariable UUID id) {
        return ResponseEntity.ok(itemAppService.getItemById(id));
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("@workSecurity.canAccessGroup(#groupId, 'ITEM:VIEW')")
    public ResponseEntity<List<ItemResponse>> getItemsByGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(itemAppService.getItemsByGroup(groupId));
    }

    @GetMapping("/board/{boardId}")
    @PreAuthorize("@workSecurity.canAccessBoard(#boardId, 'ITEM:VIEW')")
    public ResponseEntity<List<ItemResponse>> getItemsByBoard(@PathVariable UUID boardId) {
        return ResponseEntity.ok(itemAppService.getItemsByBoard(boardId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessItem(#id, 'ITEM:EDIT')")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable UUID id,
            @RequestParam String name) {
        return ResponseEntity.ok(itemAppService.updateItemName(id, name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@workSecurity.canAccessItem(#id, 'ITEM:DELETE')")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        itemAppService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
