package com.mc.controller.http.main;

import com.mc.application.model.item.*;
import com.mc.application.service.item.ItemAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemAppService itemAppService;

    /**
     * Create a new item
     *
     * @param request the create item request containing boardId, groupId, and name
     * @return created item response
     */
    @PostMapping
    @PreAuthorize("hasPermission(#request.boardId, 'Board', 'ITEM:CREATE')")
    public ResponseEntity<CreateItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        log.info("Creating item in board {} group {}", request.getBoardId(), request.getGroupId());
        return ResponseEntity.ok(itemAppService.createItem(request));
    }

    /**
     * Get item by ID
     *
     * @param id the ID of the item
     * @return item details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:VIEW')")
    public ResponseEntity<GetItemResponse> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemAppService.getItem(id));
    }

    /**
     * Update an item
     *
     * @param id the ID of the item
     * @param request the update request
     * @return updated item response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:EDIT')")
    public ResponseEntity<UpdateItemResponse> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateItemRequest request) {
        request.setItemId(id);
        return ResponseEntity.ok(itemAppService.updateItem(request));
    }

    /**
     * Delete (soft delete) an item
     *
     * @param id the ID of the item
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Item', 'ITEM:DELETE')")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        DeleteItemRequest request = new DeleteItemRequest();
        request.setItemId(id);
        itemAppService.deleteItem(request);
        return ResponseEntity.ok("Item deleted successfully");
    }

    /**
     * Get all items in a group
     *
     * @param groupId the ID of the group
     * @return list of items
     */
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasPermission(#groupId, 'Group', 'ITEM:VIEW')")
    public ResponseEntity<List<GetItemResponse>> getItemsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(itemAppService.getItemsByGroup(groupId));
    }

    /**
     * Get all items in a board
     *
     * @param boardId the ID of the board
     * @return list of items
     */
    @GetMapping("/board/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'Board', 'ITEM:VIEW')")
    public ResponseEntity<List<GetItemResponse>> getItemsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(itemAppService.getItemsByBoard(boardId));
    }

}
