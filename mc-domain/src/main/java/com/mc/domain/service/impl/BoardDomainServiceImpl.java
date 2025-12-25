package com.mc.domain.service.impl;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.*;
import com.mc.domain.model.enums.BoardColumnType;
import com.mc.domain.model.enums.BoardType;
import com.mc.domain.repository.*;
import com.mc.domain.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardDomainServiceImpl implements BoardDomainService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final ColumnRepository columnRepository;
    private final ItemRepository itemRepository;
    private final ColumnValueRepository columnValueRepository;

    @Override
    public List<Board> getBoardsForUser(Long userId) {
        return boardRepository.findAllByUserId(userId);
    }

    @Transactional
    public Board createBoard(Long userId, Long workspaceId, String name, String purpose, String type) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Workspace currentWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace", "id", workspaceId));

        Board newBoard = new Board();
        newBoard.setName(name);
        newBoard.setType(BoardType.valueOf(type));
        newBoard.setPurpose(purpose);
        newBoard.setCreatedBy(currentUser);
        newBoard.setWorkspace(currentWorkspace);
        Board savedBoard = boardRepository.save(newBoard);

        // Create default columns
        createColumn(newBoard, currentUser, newBoard.getPurpose(), BoardColumnType.TEXT, 1000.0);
        createColumn(newBoard, currentUser, "Person", BoardColumnType.PERSON, 2000.0);
        createColumn(newBoard, currentUser, "Status", BoardColumnType.STATUS, 3000.0);
        createColumn(newBoard, currentUser, "Date", BoardColumnType.DATE, 4000.0);

        // Create default task groups
        createTaskGroup(newBoard, currentUser, "To Do", "#FF5733", 1000.0);
        createTaskGroup(newBoard, currentUser, "In Progress", "#33C1FF", 2000.0);
        createTaskGroup(newBoard, currentUser, "Done", "#75FF33", 3000.0);

        return savedBoard;

    }

    private void createColumn(Board board, User creator, String title, BoardColumnType type, double position) {
        Column column = new Column();
        column.setTitle(title);
        column.setType(type);
        column.setBoard(board);
        column.setCreatedBy(creator);
        column.setPosition(position);
        columnRepository.save(column);
    }

    private void createTaskGroup(Board board, User creator, String title, String color, double position) {
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setTitle(title);
        taskGroup.setColor(color);
        taskGroup.setBoard(board);
        taskGroup.setCreatedBy(creator);
        taskGroup.setPosition(position);
        taskGroupRepository.save(taskGroup);

        createItem(board, taskGroup, creator, "Item " + position / 1000.0, position);
    }

    private void createItem(Board board, TaskGroup taskGroup, User creator, String name, double position) {
        Item item = new Item();
        item.setName(name);
        item.setBoard(board);
        item.setGroup(taskGroup);
        item.setCreatedBy(creator);
        item.setPosition(position);
        itemRepository.save(item);

        // Create default column values for the item
        List<Column> columns = columnRepository.findAllByBoardId(board.getId());
        for (Column column : columns) {
            // Except the first column which is the main column
            if (column.getPosition() == 1000.0) continue;

            switch (column.getType()) {
                case TEXT:
                    createColumnValue(item, column, board, null, "Default Text", null, "TEXT");
                    break;
                case PERSON:
                    createColumnValue(item, column, board, null, null, null, "PERSON");
                    break;
                case STATUS:
                    createColumnValue(item, column, board, null, "Working on it", "#FF0000", "STATUS");
                    break;
                case DATE:
                    createColumnValue(item, column, board, null, "2024-12-31", null, "DATE");
                    break;
            }
        }
    }

    private void createColumnValue(Item item, Column column, Board board, String value, String textValue, String color, String type) {
        ColumnValue columnValue = new ColumnValue();
        columnValue.setItem(item);
        columnValue.setColumn(column);
        columnValue.setBoard(board);
        columnValue.setValue(value);
        columnValue.setTextValue(textValue);
        columnValue.setColor(color);
        columnValue.setType(type);
        columnValueRepository.save(columnValue);
    }

    @Transactional
    public void trashBoard(Long currentUserId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));

        board.setDeletedAt(new Date());
        board.setDeletedBy(currentUser);
        boardRepository.save(board);
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public void deleteBoardPermanently(Long boardId, Long userId) {
        boardRepository.deletePhysical(boardId);
    }

}
