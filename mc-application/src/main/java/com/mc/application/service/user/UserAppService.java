package com.mc.application.service.user;

import com.mc.application.model.user.UpdateProfileRequest;
import com.mc.domain.model.entity.User;

public interface UserAppService {

    User updateProfile(UpdateProfileRequest request);

}
