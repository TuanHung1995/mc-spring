package com.mc.application.work.service.impl;

import com.mc.application.work.dto.request.*;
import com.mc.application.work.dto.response.*;
import com.mc.application.work.mapper.ColumnValueDtoMapper;
import com.mc.application.work.service.BoardAppService;
import com.mc.domain.core.event.BoardChangedEvent;
import com.mc.domain.core.exception.BusinessLogicException;
import com.mc.domain.core.exception.ResourceNotFoundException;
import com.mc.domain.work.model.entity.*;
import com.mc.domain.work.model.enums.BoardColumnType;
import com.mc.domain.work.model.enums.BoardType;
import com.mc.domain.work.port.WorkUserContextPort;
import com.mc.domain.work.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BoardAppServiceImpl — Application Service (Work Context)
 *
 * <p>Orchestrates Board and Board-initialization use cases.
 * Board creation initializes 4 columns + 3 task groups + 1 default item per group
 * within a single transaction, preserving the legacy behavior.</p>
 *
 * <p>All user-identity resolution is via {@link WorkUserContextPort#getCurrentUser()} ()}.
 * The work context uses Long user IDs (legacy IAM table).</p>
 */
@Service
@RequiredArgsConstructor
public class BoardAppServiceImpl implements BoardAppService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final BoardColumnRepository columnRepository;
    private final ItemRepository itemRepository;
    private final ColumnValueRepository columnValueRepository;
    private final WorkUserContextPort workUserContextPort;
    private final ApplicationEventPublisher eventPublisher;

    private final ColumnValueDtoMapper columnValueDtoMapper;

    // =================================================================
    // BOARD CRUD
    // =================================================================

    @Override
    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request) {
        UUID userId = workUserContextPort.getCurrentUser().id();

        BoardType type = parseBoardType(request.getType());
        Board board = Board.create(request.getName(), type, request.getPurpose(),
                userId, request.getWorkspaceId(), request.getTeamId());
        Board saved = boardRepository.save(board);

        addBoardMember(saved.getId(), userId, 1L); // Add creator as member with default role (e.g., Owner)

        // Initialize default structure (columns + groups + items) — same as legacy behavior
        initializeDefaultBoardStructure(saved, userId);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardResponse getBoardById(UUID boardId) {
        return boardRepository.findById(boardId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardsForCurrentUser() {
        UUID userId = workUserContextPort.getCurrentUser().id();
        return boardRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void trashBoard(UUID boardId) {
        UUID userId = workUserContextPort.getCurrentUser().id();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        board.trash(userId);
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public void deleteBoardPermanently(UUID boardId) {
        boardRepository.deletePhysical(boardId);
    }

    /**
     * INLINE EDIT — updates a single field on a board element.
     * Discriminates by {@code request.getType()}: TASK_GROUP | COLUMN | ITEM.
     */
    @Override
    @Transactional
    public void updateBoardElement(UpdateBoardElementRequest request) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        UUID boardId;
        switch (request.getType()) {
            case "TASK_GROUP" -> {
                TaskGroup group = taskGroupRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", request.getTargetId()));
                group.update(request.getValue(), request.getColor(), userId);
                taskGroupRepository.save(group);
                boardId = group.getBoardId();
            }
            case "ITEM" -> {
                Item item = itemRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new ResourceNotFoundException("Item", "id", request.getTargetId()));
                item.rename(request.getValue(), userId);
                itemRepository.save(item);
                boardId = item.getBoardId();
            }
            case "COLUMN_VALUE" -> {
                ColumnValue columnValue = columnValueRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new ResourceNotFoundException("Column Value", "id", request.getTargetId()));

                columnValue.updateValue(request.getValue(), request.getValue(), request.getColor());
                columnValueRepository.save(columnValue);
                boardId = columnValue.getBoardId();
            }
            default -> throw new BusinessLogicException("Unsupported element type: " + request.getType());
        }
        publishBoardEvent("ELEMENT_UPDATED", request.getTargetId(), boardId);
    }

    @Override
    @Transactional
    public List<ColumnValueResponse> getColumnValuesByBoardId(UUID boardId) {
        return columnValueRepository.findByBoardId(boardId).stream()
                .map(columnValueDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =================================================================
    // DRAG-AND-DROP REORDER
    // =================================================================

    @Override
    @Transactional
    public void reorderGroup(ReorderRequest request) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        double newPos = calculatePosition(
                taskGroupRepository.getPositionById(request.getPreviousId()),
                taskGroupRepository.getPositionById(request.getNextId())
        );
        TaskGroup group = taskGroupRepository.findById(request.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("TaskGroup", "id", request.getTargetId()));
        group.moveTo(newPos, userId);
        taskGroupRepository.save(group);
        publishBoardEvent("GROUP_REORDER", group.getId(), group.getBoardId());
    }

    @Override
    @Transactional
    public void reorderColumn(ReorderColumnRequest request) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        double newPos = calculatePosition(
                columnRepository.getPositionById(request.getPreviousId()),
                columnRepository.getPositionById(request.getNextId())
        );
        BoardColumn column = columnRepository.findById(request.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("BoardColumn", "id", request.getTargetId()));
        column.moveTo(newPos, userId);
        columnRepository.save(column);
        publishBoardColumnEvent(column.getId(), column.getBoardId());
    }

    @Override
    @Transactional
    public void reorderItem(ReorderRequest request) {

        UUID userId = workUserContextPort.getCurrentUser().id();

        double newPos = calculatePosition(
                itemRepository.getPositionById(request.getPreviousId()),
                itemRepository.getPositionById(request.getNextId())
        );
        Item item = itemRepository.findById(request.getTargetId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", request.getTargetId()));
        item.moveTo(newPos, request.getTargetGroupId(), userId);
        itemRepository.save(item);
        publishBoardEvent("ITEM_REORDER", item.getId(), item.getBoardId());
    }

    // =================================================================
    // PRIVATE HELPERS
    // =================================================================

    /**
     * Initializes a new Board's default structure in one transaction.
     * Creates 4 columns, 3 task groups, and 1 item per group (with column values).
     */
    private void initializeDefaultBoardStructure(Board board, UUID userId) {
        // Create default columns
        BoardColumn textCol   = createColumn(board.getId(), board.getWorkspaceId(), board.getTeamId(), board.getPurpose() != null ? board.getPurpose() : "Task", BoardColumnType.TEXT,   1000.0, userId);
        BoardColumn personCol = createColumn(board.getId(), board.getWorkspaceId(), board.getTeamId(), "Person",  BoardColumnType.PERSON, 2000.0, userId);
        BoardColumn statusCol = createColumn(board.getId(), board.getWorkspaceId(), board.getTeamId(), "Status",  BoardColumnType.STATUS, 3000.0, userId);
        BoardColumn dateCol   = createColumn(board.getId(), board.getWorkspaceId(), board.getTeamId(), "Date",    BoardColumnType.DATE,   4000.0, userId);

        List<BoardColumn> columns = List.of(textCol, personCol, statusCol, dateCol);

        // Create default task groups with one item each
        createDefaultGroup(board.getId(), board.getWorkspaceId(), board.getTeamId(), "To Do",       "#FF5733", 1000.0, userId, columns, board.getId());
        createDefaultGroup(board.getId(), board.getWorkspaceId(), board.getTeamId(), "In Progress", "#33C1FF", 2000.0, userId, columns, board.getId());
        createDefaultGroup(board.getId(), board.getWorkspaceId(), board.getTeamId(), "Done",        "#75FF33", 3000.0, userId, columns, board.getId());
    }

    private BoardColumn createColumn(UUID boardId, UUID workspaceId, UUID teamId, String title, BoardColumnType type, double pos, UUID userId) {
        return columnRepository.save(BoardColumn.create(boardId, title, type, pos, userId, workspaceId, teamId));
    }

    private void createDefaultGroup(UUID boardId, UUID workspaceId, UUID teamId, String title, String color, double pos,
                                    UUID userId, List<BoardColumn> columns, UUID masterBoardId) {
        TaskGroup group = taskGroupRepository.save(TaskGroup.create(boardId, workspaceId, teamId, title, color, pos, userId));

        Item item = itemRepository.save(Item.create(boardId, group.getId(), workspaceId, teamId,
                "Item " + (int)(pos / 1000), pos, userId));

        // Create default column values for the item (skip the first TEXT column — it's the name)
        for (BoardColumn col : columns) {
            if (col.getPosition() == 1000.0) continue; // skip primary column
            String textVal = switch (col.getType()) {
                case TEXT   -> "Default Text";
                case STATUS -> "Working on it";
                case DATE   -> "2024-12-31";
                default     -> null;
            };
            String colorVal = col.getType() == BoardColumnType.STATUS ? "#FF0000" : null;
            columnValueRepository.save(
                    ColumnValue.createDefault(item.getId(), col.getId(), masterBoardId, group.getId(), workspaceId, teamId,
                            null, textVal, colorVal, col.getType().name()));
        }
    }

    /**
     * Calculates the new fractional position between two neighboring positions.
     * Uses midpoint strategy with 65536 base gap.
     */
    private double calculatePosition(Double prevPos, Double nextPos) {
        if (prevPos == null && nextPos == null) return 65536.0;
        if (prevPos == null) return nextPos / 2.0;
        if (nextPos == null) return prevPos + 65536.0;
        return (prevPos + nextPos) / 2.0;
    }

    private void publishBoardEvent(String eventType, UUID entityId, UUID boardId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", eventType);
        payload.put("entityId", entityId);
        payload.put("boardId", boardId);
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    private void publishBoardColumnEvent(Long columnId, UUID boardId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "COLUMN_UPDATED");
        payload.put("entityId", columnId);
        payload.put("boardId", boardId);
        eventPublisher.publishEvent(new BoardChangedEvent(boardId, payload));
    }

    private BoardType parseBoardType(String type) {
        try {
            return type != null ? BoardType.valueOf(type.toUpperCase()) : BoardType.PUBLIC;
        } catch (IllegalArgumentException e) {
            return BoardType.PUBLIC;
        }
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(
                board.getId(), board.getName(), board.getDescription(),
                board.getType() != null ? board.getType().name() : null,
                board.getPurpose(), board.getWorkspaceId(), board.getTeamId(), board.getCreatedById(),
                board.getCreatedAt(), board.getUpdatedAt()
        );
    }

    private void addBoardMember(UUID boardId, UUID userId, Long roleId) {
        boardMemberRepository.save(BoardMember.addMember(boardId, userId, roleId));
    }
}
