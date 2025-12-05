package com.mc.infrastructure.constant;

import com.mc.domain.model.enums.RoleType;

import java.util.ArrayList;
import java.util.List;

public class UtilMethod {

    public static List<String> roleList() {
        List<String> roleList = new ArrayList<>();
        for (RoleType role : RoleType.values()) {
            roleList.add(role.name());
        }

        return roleList;
    }

}
