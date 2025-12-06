package com.mc.application.service.user;

import com.mc.application.model.user.UpdateProfileRequest;
import com.mc.application.model.user.UpdateProfileResponse;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.domain.model.entity.User;

public interface UserAppService {

    UpdateProfileResponse updateProfile(UpdateProfileRequest request);

    UserProfileResponse getMyProfile();

}
