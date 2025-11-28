package com.mc.controller.http.main;

import com.mc.domain.model.entity.Board;
import com.mc.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;

    // CHỈ user nào có quyền 'BOARD:VIEW' trên Board có ID = boardId mới được gọi API này
    @GetMapping("/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'BOARD:VIEW')")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        return ResponseEntity.ok(board);
    }

    // CHỈ user nào có quyền 'BOARD:DELETE' trên Board đó mới được xóa
    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasPermission(#boardId, 'BOARD:DELETE')")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        // ... logic xóa
        return ResponseEntity.ok().build();
    }
}
