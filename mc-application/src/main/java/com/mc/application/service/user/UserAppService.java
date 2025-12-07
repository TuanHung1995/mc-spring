package com.mc.application.service.user;

import com.mc.application.model.user.*;
import com.mc.domain.model.entity.User;

public interface UserAppService {

    UpdateProfileResponse updateProfile(UpdateProfileRequest request);

    UserProfileResponse getMyProfile();

    ChangePasswordResponse changePassword(ChangePasswordRequest request);

}
