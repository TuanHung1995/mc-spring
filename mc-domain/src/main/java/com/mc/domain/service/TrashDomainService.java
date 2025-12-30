package com.mc.domain.service;

import com.mc.domain.model.dto.TrashItem;
import java.util.List;

public interface TrashDomainService {
    
    /**
     * Retrieves all items (Boards, etc.) located in the trash for a specific user.
     * @param userId the ID of the user request.
     * @return List of TrashItem
     */
    List<TrashItem> getAllTrashItems(Long userId);

    /**
     * Restores a board from trash.
     * @param boardId the ID of the board to restore.
     */
    void restoreBoard(Long boardId);

}
