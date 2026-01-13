package com.mc.controller.http.iam;

import com.mc.application.iam.dto.request.*;
import com.mc.application.iam.dto.response.*;
import com.mc.application.iam.service.UserAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * User Controller - IAM Bounded Context
 * Handles user profile management endpoints using Clean Architecture.
 */
@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class IamUserController {

    private final UserAppService userAppService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        UserResponse response = userAppService.getMyProfile();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        UserResponse response = userAppService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = userAppService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        MessageResponse response = userAppService.changePassword(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().length() <= 1) {
            return ResponseEntity.ok(List.of());
        }
        List<UserResponse> response = userAppService.searchUsers(keyword);
        return ResponseEntity.ok(response);
    }
}
