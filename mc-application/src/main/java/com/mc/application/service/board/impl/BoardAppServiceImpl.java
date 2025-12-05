package com.mc.application.service.board.impl;

import com.mc.application.model.board.ReorderRequest;
import com.mc.application.service.board.BoardAppService;
import com.mc.domain.model.entity.Board;
import com.mc.domain.port.RealTimeUpdatePort;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardAppServiceImpl implements BoardAppService {

    private final BoardDomainService boardDomainService;
    private final ItemDomainService itemDomainService;
    private final TaskGroupDomainService taskGroupDomainService;
    private final ColumnDomainService columnDomainService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RealTimeUpdatePort realTimeUpdatePort; // [MỚI] Inject Port

    @Value("${constants.frontend}")
    private String frontendUrl;

    @Override
    public List<Board> getBoardsForUser() {
        // 1. Lấy thông tin User hiện tại từ Security Context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId;

        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new IllegalStateException("User not authenticated properly");
        }

        // 2. Gọi Domain Service để lấy danh sách
        return boardDomainService.getBoardsForUser(userId);
    }

    public void reorderGroup(ReorderRequest request) {
        taskGroupDomainService.reorderGroup(request.getTargetId(), request.getPreviousId(), request.getNextId());
    }

    public void reorderColumn(ReorderRequest request) {
        columnDomainService.reorderColumn(request.getTargetId(), request.getPreviousId(), request.getNextId());
    }

    public void reorderItem(ReorderRequest request) {
        itemDomainService.reorderItem(
                request.getTargetId(),
                request.getTargetGroupId(),
                request.getPreviousId(),
                request.getNextId()
        );
    }

}
