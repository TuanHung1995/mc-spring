package com.mc.infrastructure.work.security;

import com.mc.infrastructure.iam.security.userdetails.IamUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component("workSecurity")
public class WorkPermissionEvaluator {

    /**
     * Extracts the User ID from the SecurityContext
     */
    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof IamUserDetails userDetails) {
            return userDetails.getUserId();
        }
        log.warn("Could not extract user ID from SecurityContext - missing or invalid authentication object");
        return null;
    }

    public boolean canAccessWorkspace(Long workspaceId, String permission) {
        UUID userId = getCurrentUserId();
        if (userId == null) return false;
        
        log.debug("Checking permission {} for user {} on Workspace {}", permission, userId, workspaceId);
        // TODO: Implement actual lookup (e.g. WorkspaceMember lookup or delegation to Org security)
        return true;
    }

    public boolean canAccessBoard(Long boardId, String permission) {
        UUID userId = getCurrentUserId();
        if (userId == null) return false;
        
        log.debug("Checking permission {} for user {} on Board {}", permission, userId, boardId);
        // TODO: Implement actual lookup logic against Work context DB
        return true;
    }

    public boolean canAccessGroup(Long groupId, String permission) {
        UUID userId = getCurrentUserId();
        if (userId == null) return false;
        
        log.debug("Checking permission {} for user {} on Group {}", permission, userId, groupId);
        // TODO: Implement actual lookup logic (e.g. traverse Group -> Board -> Workspace -> Role)
        return true;
    }

    public boolean canAccessColumn(Long columnId, String permission) {
        UUID userId = getCurrentUserId();
        if (userId == null) return false;
        
        log.debug("Checking permission {} for user {} on Column {}", permission, userId, columnId);
        // TODO: Implement actual lookup logic
        return true;
    }

    public boolean canAccessItem(Long itemId, String permission) {
        UUID userId = getCurrentUserId();
        if (userId == null) return false;
        
        log.debug("Checking permission {} for user {} on Item {}", permission, userId, itemId);
        // TODO: Implement actual lookup logic
        return true;
    }
}
