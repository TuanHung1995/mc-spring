package com.mc.application.model.user;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String fullName;
    private String phone;
    private String birthday;
    private String address;
    private String jobTitle;

}
