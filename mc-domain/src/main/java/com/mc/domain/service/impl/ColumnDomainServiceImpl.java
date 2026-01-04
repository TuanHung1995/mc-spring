package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.Column;
import com.mc.domain.model.entity.User;
import com.mc.domain.model.enums.BoardColumnType;
import com.mc.domain.repository.BoardRepository;
import com.mc.domain.repository.ColumnRepository;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.ColumnDomainService;
import com.mc.domain.service.util.PositionCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnDomainServiceImpl implements ColumnDomainService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PositionCalculationService positionCalculationService;

    @Override
    @Transactional
    public Column createColumn(Long boardId, String title, BoardColumnType type, Long userId) {
        // Validate board exists
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        // Get creator
        User creator = userRepository.findById(userId)
                .orElse(null);
        
        // Calculate position (append to end)
        Double maxPosition = columnRepository.getMaxPositionByBoardId(boardId);
        double newPosition = (maxPosition != null) ? maxPosition + 65536 : 65536;
        
        // Create column
        Column column = new Column();
        column.setTitle(title);
        column.setType(type);
        column.setBoard(board);
        column.setCreatedBy(creator);
        column.setPosition(newPosition);
        column.setWidth(150); // Default width
        column.setHidden(false);
        column.setCreatedAt(new Date());
        
        return columnRepository.save(column);
    }

    @Override
    @Transactional(readOnly = true)
    public Column getColumnById(Long columnId) {
        return columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Column> getColumnsByBoardId(Long boardId) {
        // Validate board exists
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        
        return columnRepository.findAllByBoardId(boardId);
    }

    @Override
    @Transactional
    public Column updateColumnDetails(Long columnId, String newTitle) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));

        if (newTitle != null) column.setTitle(newTitle);

        return columnRepository.save(column);
    }

    @Override
    @Transactional
    public Column reorderColumn(Long columnId, Long prevItemId, Long nextItemId) {
        // 1. Tính vị trí mới
        Double prevPos = (prevItemId != null) ? columnRepository.getPosition(prevItemId) : null;
        Double nextPos = (nextItemId != null) ? columnRepository.getPosition(nextItemId) : null;

        double newPos = positionCalculationService.calculateNewPosition(prevPos, nextPos);

        // 2. Lấy Column
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));

        // 4. Cập nhật vị trí
        column.setPosition(newPos);
        return columnRepository.save(column);
    }

    @Override
    @Transactional
    public void deleteColumn(Long columnId, Long userId) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new ResourceNotFoundException("Column", "id", columnId));

        User currentUser = userRepository.findById(userId)
                .orElse(null);

        // Set soft delete fields
        column.setDeletedAt(new Date());
        column.setDeletedBy(currentUser);
        
        // Soft delete
        columnRepository.delete(column);
    }

}
