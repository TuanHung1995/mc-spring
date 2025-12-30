package com.mc.controller.http;

import com.mc.application.service.TrashAppService;
import com.mc.domain.model.dto.TrashItem;
import com.mc.infrastructure.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrashControllerTest {

    @Mock
    private TrashAppService trashAppService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TrashController trashController;

    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        // Setup SecurityContext to mock authenticated user
        userPrincipal = new UserPrincipal(1L, "test@example.com", "password", null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getTrashItems_shouldReturnListOfItems() {
        // Arrange
        Long userId = 1L;
        TrashItem item = TrashItem.builder()
                .id(100L)
                .name("Deleted Board")
                .type("BOARD")
                .build();
        
        when(trashAppService.getTrashItems(userId)).thenReturn(Collections.singletonList(item));

        // Act
        ResponseEntity<List<TrashItem>> response = trashController.getTrashItems(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Deleted Board", response.getBody().get(0).getName());
        
        verify(trashAppService).getTrashItems(userId);
    }

    @Test
    void restoreItem_shouldCallServiceAndReturnSuccess() {
        // Arrange
        String type = "BOARD";
        Long id = 100L;

        // Act
        ResponseEntity<?> response = trashController.restoreItem(type, id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item restored successfully", response.getBody());
        
        verify(trashAppService).restoreItem(type, id);
    }

    @Test
    void deleteItemPermanently_shouldCallServiceAndReturnSuccess() {
        // Arrange
        String type = "BOARD";
        Long id = 100L;

        // Act
        ResponseEntity<?> response = trashController.deleteItemPermanently(type, id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item deleted permanently", response.getBody());
        
        verify(trashAppService).deleteItemPermanently(type, id);
    }
}
