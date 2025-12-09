package com.mc.application.mapper;

import com.mc.application.model.auth.RegisterResponse;
import com.mc.application.model.user.UpdateProfileResponse;
import com.mc.application.model.user.UserProfileResponse;
import com.mc.domain.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Map từ Entity User -> RegisterResponse
    // Nếu tên trường giống nhau, MapStruct tự map.
    // Nếu khác nhau, dùng @Mapping(source = "...", target = "...")
    RegisterResponse toRegisterResponse(User user);
    UpdateProfileResponse toUpdateProfileResponse(User user);
    UserProfileResponse toUserProfileResponse(User user);

    // Ví dụ mapping khác
    // @Mapping(target = "password", ignore = true) // Bỏ qua password
    // User toEntity(RegisterRequest request);

}
