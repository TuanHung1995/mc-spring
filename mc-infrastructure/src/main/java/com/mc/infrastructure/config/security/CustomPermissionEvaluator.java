package com.mc.infrastructure.config.security;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.BoardMemberRepository;
import com.mc.domain.repository.TeamMemberRepository;
import com.mc.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final BoardMemberRepository boardMemberRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
                return false;
            }

            Long boardId = (Long) targetDomainObject;
            String requiredPermission = (String) permission;

            // TỐI ƯU: Lấy User ID trực tiếp từ Principal (Memory) thay vì Query DB
            Object principal = authentication.getPrincipal();
            Long userId;

            if (principal instanceof CustomUserDetails) {
                userId = ((CustomUserDetails) principal).getUserId();
            } else {
                // Fallback cho trường hợp authentication lạ (không nên xảy ra nếu config đúng)
                return false;
            }

            // Chỉ tốn 1 Query này để check role
            Optional<Role> roleOpt = boardMemberRepository.findRoleByBoardIdAndUserId(boardId, userId);

            if (roleOpt.isPresent()) {
                Role role = roleOpt.get();
                return role.getPermissions().stream()
                        .anyMatch(p -> p.getName().equals(requiredPermission));
            }

            return false;
        }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (auth == null || targetId == null || permission == null) return false;

        Long userId = getUserId(auth);
        if (userId == null) return false;

        Long resourceId = (Long) targetId;
        String requiredPerm = (String) permission;

        Optional<Role> roleOpt = Optional.empty();

        // 1. Phân loại Resource để tìm Role tương ứng
        if ("Board".equalsIgnoreCase(targetType)) {
            roleOpt = boardMemberRepository.findRoleByBoardIdAndUserId(resourceId, userId);
        } else if ("Team".equalsIgnoreCase(targetType)) {
            roleOpt = teamMemberRepository.findRoleByTeamIdAndUserId(resourceId, userId);
        }

        // 2. Check Permission trong Role
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            return role.getPermissions().stream()
                    .anyMatch(p -> p.getName().equals(requiredPerm));
        }

        return false;
    }

    private Long getUserId(Authentication auth) {
        if (auth.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) auth.getPrincipal()).getUserId();
        }
        return null;
    }
}
