package com.mc.domain.service.impl;

import com.mc.domain.model.dto.TrashItem;
import com.mc.domain.model.entity.Board;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TrashDomainServiceImplTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TrashDomainServiceImpl trashDomainService;

    @Test
    void getAllTrashItems_shouldReturnListOfTrashItems() {
        // Arrange
        Long userId = 1L;
        User deletedBy = new User();
        deletedBy.setId(userId);
        deletedBy.setFullName("Test User");

        Board board = new Board();
        board.setId(100L);
        board.setName("Deleted Board");
        board.setDeletedAt(new Date());
        board.setDeletedBy(deletedBy);

        Mockito.when(boardRepository.findAllDeletedBoards(userId)).thenReturn(Collections.singletonList(board));

        // Act
        List<TrashItem> result = trashDomainService.getAllTrashItems(userId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        
        TrashItem item = result.get(0);
        Assertions.assertEquals(100L, item.getId());
        Assertions.assertEquals("BOARD", item.getType());
        Assertions.assertEquals("Deleted Board", item.getName());
        Assertions.assertEquals("Test User", item.getDeletedByName());
    }

    @Test
    void restoreBoard_shouldCallRepository() {
        // Arrange
        Long boardId = 100L;

        // Act
        trashDomainService.restoreBoard(boardId);

        // Assert
        Mockito.verify(boardRepository, Mockito.times(1)).restoreBoard(boardId);
    }
}
