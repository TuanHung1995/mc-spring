package com.mc.application.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileResponse {

    private String fullName;
    private String phone;
    private String birthday;
    private String address;
    private String jobTitle;

}
