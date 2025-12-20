package com.mc.infrastructure.config.security;

import com.mc.domain.model.entity.*;
import com.mc.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final BoardMemberRepository boardMemberRepo;
    private final TeamMemberRepository teamMemberRepo;
    private final WorkspaceMemberRepository workspaceMemberRepo;
    private final ApartmentMemberRepository apartmentMemberRepo;

    private final BoardRepository boardRepo;
    private final WorkspaceRepository workspaceRepo;
    private final ApartmentRepository apartmentRepo;

    private final TaskGroupRepository taskGroupRepo;
    private final ColumnRepository columnRepo;
    private final ItemRepository itemRepo;

    private static final String TYPE_TEAM = "Team";
    private static final String TYPE_WORKSPACE = "Workspace";
    private static final String TYPE_BOARD = "Board";
    private static final String TYPE_GROUP = "Group";
    private static final String TYPE_COLUMN = "Column";
    private static final String TYPE_ITEM = "Item";

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        if (targetDomainObject instanceof Long) {
            return hasPermission(auth, (Long) targetDomainObject, "Unknown", permission);
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

        return switch (targetType) {
            case TYPE_TEAM -> checkTeamPermission(resourceId, userId, requiredPerm);
            case TYPE_WORKSPACE -> checkWorkspacePermission(resourceId, userId, requiredPerm);
            case TYPE_BOARD -> checkBoardPermission(resourceId, userId, requiredPerm);
            case TYPE_GROUP, TYPE_COLUMN, TYPE_ITEM -> checkSubBoardEntityPermission(resourceId, targetType, userId, requiredPerm);
            default -> {
                log.warn("Unknown targetType for permission check: {}", targetType);
                yield false;
            }
        };
    }

    // --- LEVEL 1: TEAM (Highest Level) ---
    private boolean checkTeamPermission(Long teamId, Long userId, String permission) {
        Optional<Role> roleOpt = teamMemberRepo.findRoleByTeamIdAndUserId(teamId, userId);
        return roleOpt.isPresent() && hasPrivilege(roleOpt.get(), permission);
    }

    // --- LEVEL 2: WORKSPACE (Inherits from Team) ---
    private boolean checkWorkspacePermission(Long workspaceId, Long userId, String permission) {
        // 1. Check trực tiếp tại Workspace
        Optional<Role> roleOpt = workspaceMemberRepo.findRoleByWorkspaceIdAndUserId(workspaceId, userId);
        if (roleOpt.isPresent() && hasPrivilege(roleOpt.get(), permission)) {
            return true;
        }

        // 2. Nếu không có quyền tại Workspace, check quyền tại Team cha (Hierarchy)
        return workspaceRepo.findById(workspaceId)
                .map(Workspace::getTeam)
                .map(team -> checkTeamPermission(team.getId(), userId, permission))
                .orElse(false);
    }

    // --- LEVEL 2.1: APARTMENT (Inherits from Workspace)
    private boolean checkApartmentPermission(Long apartmentId, Long userId, String permission) {
        Optional<Role> roleOpt = teamMemberRepo.findRoleByTeamIdAndUserId(apartmentId, userId);

        if (roleOpt.isPresent() && hasPrivilege(roleOpt.get(), permission)) {
            return true;
        }

        return apartmentRepo.findById(apartmentId)
                .map(Apartment::getWorkspace)
                .map(ws -> checkWorkspacePermission(ws.getId(), userId, permission))
                .orElse(false);
    }

    // --- LEVEL 3: BOARD (Inherits from Workspace) ---
    private boolean checkBoardPermission(Long boardId, Long userId, String permission) {
        // 1. Check trực tiếp tại Board
        Optional<Role> roleOpt = boardMemberRepo.findRoleByBoardIdAndUserId(boardId, userId);
        if (roleOpt.isPresent() && hasPrivilege(roleOpt.get(), permission)) {
            return true;
        }

        // 2. Nếu không có quyền tại Board, check quyền tại Workspace cha (Hierarchy)
        return boardRepo.findById(boardId)
                .map(Board::getWorkspace)
                .map(ws -> checkWorkspacePermission(ws.getId(), userId, permission)) // Đệ quy lên Workspace
                .orElse(false);
    }

    // --- LEVEL 4: SUB-ENTITIES (Group, Column, Item -> Resolve to Board) ---
    private boolean checkSubBoardEntityPermission(Long entityId, String entityType, Long userId, String permission) {
        Long parentBoardId = null;

        if (TYPE_GROUP.equalsIgnoreCase(entityType)) {
            parentBoardId = taskGroupRepo.findBoardIdByGroupId(entityId).orElse(null);
        } else if (TYPE_COLUMN.equalsIgnoreCase(entityType)) {
            parentBoardId = columnRepo.findBoardIdByColumnId(entityId).orElse(null);
        } else if (TYPE_ITEM.equalsIgnoreCase(entityType)) {
            parentBoardId = itemRepo.findBoardIdByItemId(entityId).orElse(null);
        }

        if (parentBoardId != null) {
            // Tái sử dụng logic check Board (bao gồm cả việc thừa kế từ Workspace/Team)
            return checkBoardPermission(parentBoardId, userId, permission);
        }

        return false;
    }

    // --- HELPER METHODS ---

    private boolean hasPrivilege(Role role, String permission) {
        return role.getPermissions().stream()
                .anyMatch(p -> p.getName().equals(permission));
    }

    private Long getUserId(Authentication auth) {
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}