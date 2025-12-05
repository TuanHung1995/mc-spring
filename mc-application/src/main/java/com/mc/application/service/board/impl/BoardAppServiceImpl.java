package com.mc.application.service.board.impl;

import com.mc.application.service.board.BoardAppService;
import com.mc.domain.model.entity.Board;
import com.mc.domain.service.BoardDomainService;
import com.mc.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardAppServiceImpl implements BoardAppService {

    private final BoardDomainService boardDomainService;

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

}
