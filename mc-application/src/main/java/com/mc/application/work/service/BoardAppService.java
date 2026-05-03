package com.mc.application.work.service;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * BoardAppService — Application Service Port (Work Context)
 *
 * <p>Covers Board CRUD, inline element editing, and drag-and-drop reorder.</p>
 */
public interface BoardAppService {

    BoardResponse createBoard(CreateBoardRequest request);
    BoardResponse getBoardById(UUID boardId);
    List<BoardResponse> getBoardsForCurrentUser();

    /** Soft-trashes a board (moves to Trash). */
    void trashBoard(UUID boardId);

    /** Permanently removes a board and all its data. */
    void deleteBoardPermanently(UUID boardId);

    /** Inline field edit: updates a group title/color, column title, or item name. */
    void updateBoardElement(UpdateBoardElementRequest request);

    List<ColumnValueResponse> getColumnValuesByBoardId(UUID boardId);

    // Drag-and-drop reorder
    void reorderGroup(ReorderRequest request);
    void reorderColumn(ReorderColumnRequest request);
    void reorderItem(ReorderRequest request);
}
