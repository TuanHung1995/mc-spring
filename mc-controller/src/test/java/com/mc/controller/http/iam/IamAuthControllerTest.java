package com.mc.controller.http.iam;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;
import com.mc.application.iam.service.AuthAppService;
import com.mc.infrastructure.utils.CookieUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IamAuthControllerTest {

    @Mock
    private AuthAppService authAppService;

    @Mock
    private CookieUtils cookieUtils;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private IamAuthController iamAuthController;

    @Test
    void login_shouldReturnAuthResponseAndCookies() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
        
        ResponseCookie accessCookie = ResponseCookie.from("access_token", "access-token").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "refresh-token").build();

        when(authAppService.login(loginRequest)).thenReturn(authResponse);
        when(cookieUtils.createAccessTokenCookie("access-token")).thenReturn(accessCookie);
        when(cookieUtils.createRefreshTokenCookie("refresh-token")).thenReturn(refreshCookie);

        // Act
        ResponseEntity<AuthResponse> response = iamAuthController.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        
        HttpHeaders headers = response.getHeaders();
        assertTrue(headers.containsKey(HttpHeaders.SET_COOKIE));
        assertEquals(2, headers.get(HttpHeaders.SET_COOKIE).size());
        assertTrue(headers.get(HttpHeaders.SET_COOKIE).contains(accessCookie.toString()));
        assertTrue(headers.get(HttpHeaders.SET_COOKIE).contains(refreshCookie.toString()));
        
        verify(authAppService).login(loginRequest);
    }

    @Test
    void register_shouldReturnRegisterResponse() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest(); // Assuming default constructor or valid fields

        registerRequest.setEmail("new@example.com"); 

        RegisterResponse registerResponse = new RegisterResponse(); // Assuming constructor/builder

        when(authAppService.register(registerRequest)).thenReturn(registerResponse);

        // Act
        ResponseEntity<RegisterResponse> response = iamAuthController.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registerResponse, response.getBody());
        
        verify(authAppService).register(registerRequest);
    }

    @Test
    void forgotPassword_shouldReturnMessageResponse() {
        // Arrange
        String email = "test@example.com";
        MessageResponse messageResponse = new MessageResponse("Reset link sent", true); // Assuming constructor

        when(authAppService.forgotPassword(email)).thenReturn(messageResponse);

        // Act
        ResponseEntity<MessageResponse> response = iamAuthController.forgotPassword(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageResponse, response.getBody());
        
        verify(authAppService).forgotPassword(email);
    }

    @Test
    void resetPassword_shouldReturnMessageResponse() {
        // Arrange
        ResetPasswordRequest resetRequest = new ResetPasswordRequest();
        MessageResponse messageResponse = new MessageResponse("Password reset successful", true);

        when(authAppService.resetPassword(resetRequest)).thenReturn(messageResponse);

        // Act
        ResponseEntity<MessageResponse> response = iamAuthController.resetPassword(resetRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageResponse, response.getBody());
        
        verify(authAppService).resetPassword(resetRequest);
    }

    @Test
    void refreshToken_shouldReturnAuthResponseAndCookies_whenCookiePresent() {
        // Arrange
        String oldRefreshToken = "old-refresh-token";
        when(cookieUtils.getCookieValue(request, "refresh_token")).thenReturn(oldRefreshToken);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .build();
        
        ResponseCookie accessCookie = ResponseCookie.from("access_token", "new-access-token").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "new-refresh-token").build();

        when(authAppService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authResponse);
        when(cookieUtils.createAccessTokenCookie("new-access-token")).thenReturn(accessCookie);
        when(cookieUtils.createRefreshTokenCookie("new-refresh-token")).thenReturn(refreshCookie);

        // Act
        ResponseEntity<AuthResponse> response = iamAuthController.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        
        verify(authAppService).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    void refreshToken_shouldReturnBadRequest_whenCookieMissing() {
        // Arrange
        when(cookieUtils.getCookieValue(request, "refresh_token")).thenReturn(null);

        // Act
        ResponseEntity<AuthResponse> response = iamAuthController.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(authAppService, never()).refreshToken(any());
    }

    @Test
    void logout_shouldClearCookies() {
        // Arrange
        String accessToken = "valid-token";
        when(cookieUtils.getCookieValue(request, "access_token")).thenReturn(accessToken);

        MessageResponse messageResponse = new MessageResponse("Logged out", true);
        when(authAppService.logout(accessToken)).thenReturn(messageResponse);

        ResponseCookie deleteAccess = ResponseCookie.from("access_token", "").maxAge(0).build();
        ResponseCookie deleteRefresh = ResponseCookie.from("refresh_token", "").maxAge(0).build();

        when(cookieUtils.deleteCookie("access_token")).thenReturn(deleteAccess);
        when(cookieUtils.deleteCookie("refresh_token")).thenReturn(deleteRefresh);

        // Act
        ResponseEntity<MessageResponse> response = iamAuthController.logout(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageResponse, response.getBody());
        
        HttpHeaders headers = response.getHeaders();
        assertTrue(Objects.requireNonNull(headers.get(HttpHeaders.SET_COOKIE)).contains(deleteAccess.toString()));
        assertTrue(Objects.requireNonNull(headers.get(HttpHeaders.SET_COOKIE)).contains(deleteRefresh.toString()));
        
        verify(authAppService).logout(accessToken);
    }
    
    @Test
    void logout_shouldOnlyClearCookies_whenTokenIsNull() {
        // Arrange
        when(cookieUtils.getCookieValue(request, "access_token")).thenReturn(null);

        ResponseCookie deleteAccess = ResponseCookie.from("access_token", "").maxAge(0).build();
        ResponseCookie deleteRefresh = ResponseCookie.from("refresh_token", "").maxAge(0).build();

        when(cookieUtils.deleteCookie("access_token")).thenReturn(deleteAccess);
        when(cookieUtils.deleteCookie("refresh_token")).thenReturn(deleteRefresh);

        // Act
        ResponseEntity<MessageResponse> response = iamAuthController.logout(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody()); // Based on implementation provided: MessageResponse message = null;
        
        verify(authAppService, never()).logout(anyString());
    }

    @Test
    void unlockAccount_shouldReturnMessageResponse() {
        // Arrange
        String token = "unlock-token";
        MessageResponse messageResponse = new MessageResponse("Account unlocked", true);

        when(authAppService.unlockAccount(token)).thenReturn(messageResponse);

        // Act
        ResponseEntity<MessageResponse> response = iamAuthController.unlockAccount(token);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageResponse, response.getBody());
        
        verify(authAppService).unlockAccount(token);
    }
}
