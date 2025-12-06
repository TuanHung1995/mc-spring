package com.mc.controller.http.main;

import com.mc.application.model.board.InviteRequest;
import com.mc.application.model.board.ReorderRequest;
import com.mc.application.service.board.BoardAppService;
import com.mc.application.service.invite.InviteAppService;
import com.mc.domain.model.entity.Board;
import com.mc.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardRepository boardRepository;
    private final InviteAppService inviteAppService;

    private final BoardAppService boardAppService;

    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:VIEW')")
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'BOARD:DELETE')")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/invite")
    @PreAuthorize("hasPermission(#boardId, 'Board', 'BOARD:INVITE')")
    public ResponseEntity<?> inviteMember(@PathVariable Long boardId, @RequestBody InviteRequest request) {
        inviteAppService.inviteMember(boardId, request);
        return ResponseEntity.ok("Invitation sent");
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<?> acceptInvite(@RequestParam String token) {
        inviteAppService.acceptInvitation(token);
        return ResponseEntity.ok("Joined board successfully");
    }

    @PutMapping("/groups/reorder")
    @PreAuthorize("hasPermission(#request.targetId, 'Group', 'BOARD:EDIT')") // Cần update permission logic để check group thuộc board nào
    public ResponseEntity<?> reorderGroup(@RequestBody ReorderRequest request) {
        boardAppService.reorderGroup(request);
        return ResponseEntity.ok().build();
    }

    // 2. Kéo thả Column
    @PutMapping("/columns/reorder")
    @PreAuthorize("hasPermission(#request.targetId, 'Column', 'BOARD:EDIT')")
    public ResponseEntity<?> reorderColumn(@RequestBody ReorderRequest request) {
        boardAppService.reorderColumn(request);
        return ResponseEntity.ok().build();
    }

    // 3. Kéo thả Item (Task)
    @PutMapping("/items/reorder")
    @PreAuthorize("hasPermission(#request.targetId, 'Item', 'BOARD:EDIT')")
    public ResponseEntity<?> reorderItem(@RequestBody ReorderRequest request) {
        boardAppService.reorderItem(request);
        return ResponseEntity.ok("Reorder Successfully");
    }

    @GetMapping
    public ResponseEntity<List<Board>> getMyBoards() {
        return ResponseEntity.ok(boardAppService.getBoardsForUser());
    }
}
