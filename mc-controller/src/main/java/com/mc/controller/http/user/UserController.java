package com.mc.controller.http.user;

import com.mc.application.model.user.UpdateProfileRequest;
import com.mc.application.model.user.UpdateProfileResponse;
import com.mc.application.service.user.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
