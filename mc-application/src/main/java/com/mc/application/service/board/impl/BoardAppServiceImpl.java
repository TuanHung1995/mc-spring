package com.mc.application.service.board.impl;

import com.mc.application.model.board.ReorderRequest;
import com.mc.application.service.board.BoardAppService;
import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.Column;
import com.mc.domain.model.entity.Item;
import com.mc.domain.model.entity.TaskGroup;
import com.mc.domain.port.RealTimeUpdatePort;
import com.mc.domain.port.UserContextPort;
import com.mc.domain.service.BoardDomainService;
import com.mc.domain.service.ColumnDomainService;
import com.mc.domain.service.ItemDomainService;
import com.mc.domain.service.TaskGroupDomainService;
import com.mc.infrastructure.config.security.CustomUserDetails;
import com.mc.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardAppServiceImpl implements BoardAppService {

    private final BoardDomainService boardDomainService;
    private final ItemDomainService itemDomainService;
    private final TaskGroupDomainService taskGroupDomainService;
    private final ColumnDomainService columnDomainService;
    private final UserContextPort userContextPort;
    private final RealTimeUpdatePort realTimeUpdatePort;

    @Value("${constants.frontend}")
    private String frontendUrl;

    @Override
    public List<Board> getBoardsForUser() {
        /* Get current user ID from UserContextPort */
        return boardDomainService.getBoardsForUser(userContextPort.getCurrentUserId());
    }

    public void reorderGroup(ReorderRequest request) {
        TaskGroup updatedGroup = taskGroupDomainService.reorderGroup(
                request.getTargetId(), request.getPreviousId(), request.getNextId());

        // Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "GROUP_REORDER");
        payload.put("entityId", updatedGroup.getId());
        payload.put("newPosition", updatedGroup.getPosition());
        payload.put("boardId", updatedGroup.getBoard().getId());

        realTimeUpdatePort.sendBoardUpdate(updatedGroup.getBoard().getId(), payload);
    }

    public void reorderColumn(ReorderRequest request) {
        Column updatedColumn = columnDomainService.reorderColumn(
                request.getTargetId(), request.getPreviousId(), request.getNextId());

        // Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "COLUMN_REORDER");
        payload.put("entityId", updatedColumn.getId());
        payload.put("newPosition", updatedColumn.getPosition());
        payload.put("boardId", updatedColumn.getBoard().getId());
        
        realTimeUpdatePort.sendBoardUpdate(updatedColumn.getBoard().getId(), payload);
    }

    public void reorderItem(ReorderRequest request) {
        Item updatedItem = itemDomainService.reorderItem(
                request.getTargetId(), request.getTargetGroupId(), request.getPreviousId(), request.getNextId());

        // Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ITEM_REORDER");
        payload.put("entityId", updatedItem.getId());
        payload.put("newPosition", updatedItem.getPosition());
        payload.put("groupId", updatedItem.getGroup().getId());
        payload.put("boardId", updatedItem.getBoard().getId());

        realTimeUpdatePort.sendBoardUpdate(updatedItem.getBoard().getId(), payload);

    }

}
