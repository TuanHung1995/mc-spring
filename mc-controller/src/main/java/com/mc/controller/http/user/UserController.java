package com.mc.controller.http.user;

import com.mc.application.model.user.*;
import com.mc.application.service.user.UserAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;

    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        UpdateProfileResponse response = userAppService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        UserProfileResponse response = userAppService.getMyProfile();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ChangePasswordResponse response = userAppService.changePassword(request);

        return ResponseEntity.ok(response);
    }

}
