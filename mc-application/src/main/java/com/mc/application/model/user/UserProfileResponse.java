package com.mc.application.model.user;

import lombok.Data;

@Data
public class UserProfileResponse {

    private String fullName;
    private String phone;
    private String birthday;
    private String address;
    private String jobTitle;

}
