package com.mc.infrastructure.config.security;

import com.mc.domain.model.entity.Role;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.BoardMemberRepository;
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
    private final UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        // Logic check quyền trên Resource cụ thể (Ví dụ Board)
        // targetDomainObject ở đây có thể là BoardID (Long)
        Long boardId = (Long) targetDomainObject;
        String requiredPermission = (String) permission;

        String email = authentication.getName();
        // Lấy User ID từ email (Có thể cache lại để tối ưu)
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;

        // 1. Lấy Role của User trên Board này
        Optional<Role> roleOpt = boardMemberRepository.findRoleByBoardIdAndUserId(boardId, user.getId());

        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            // 2. Check xem Role đó có chứa Permission yêu cầu không
            return role.getPermissions().stream()
                    .anyMatch(p -> p.getName().equals(requiredPermission));
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Hỗ trợ trường hợp gọi kiểu: hasPermission(1, 'Board', 'VIEW')
        if ("Board".equals(targetType)) {
            return hasPermission(authentication, targetId, permission);
        }
        return false;
    }
}
