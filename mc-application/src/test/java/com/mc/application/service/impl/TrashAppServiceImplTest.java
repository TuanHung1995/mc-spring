package com.mc.application.service.impl;

import com.mc.application.service.impl.TrashAppServiceImpl;
import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.model.dto.TrashItem;
import com.mc.domain.service.BoardDomainService;
import com.mc.domain.service.TrashDomainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TrashAppServiceImplTest {

    @Mock
    private TrashDomainService trashDomainService;

    @Mock
    private BoardDomainService boardDomainService;

    @InjectMocks
    private TrashAppServiceImpl trashAppService;

    @Test
    void getTrashItems_shouldDelegateToDomainService() {
        // Arrange
        Long userId = 1L;
        TrashItem item = TrashItem.builder().id(100L).build();
        Mockito.when(trashDomainService.getAllTrashItems(userId)).thenReturn(Collections.singletonList(item));

        // Act
        List<TrashItem> result = trashAppService.getTrashItems(userId);

        // Assert
        Assertions.assertEquals(1, result.size());
        Mockito.verify(trashDomainService).getAllTrashItems(userId);
    }

    @Test
    void restoreItem_shouldRestoreBoard_whenTypeIsBoard() {
        // Arrange
        Long id = 100L;
        String type = "BOARD";

        // Act
        trashAppService.restoreItem(type, id);

        // Assert
        Mockito.verify(trashDomainService).restoreBoard(id);
    }

    @Test
    void restoreItem_shouldThrowException_whenTypeIsUnknown() {
        // Arrange
        Long id = 100L;
        String type = "UNKNOWN";

        // Act & Assert
        Assertions.assertThrows(BusinessLogicException.class, () -> trashAppService.restoreItem(type, id));
    }

    @Test
    void deleteItemPermanently_shouldDeleteBoard_whenTypeIsBoard() {
        // Arrange
        Long id = 100L;
        String type = "BOARD";

        // Act
        trashAppService.deleteItemPermanently(type, id);

        // Assert
        Mockito.verify(boardDomainService).deleteBoardPermanently(Mockito.eq(id), Mockito.any());
    }
}
