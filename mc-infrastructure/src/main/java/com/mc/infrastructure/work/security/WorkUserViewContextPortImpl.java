package com.mc.infrastructure.work.security;

import com.mc.domain.iam.exception.UserNotFoundException;
import com.mc.domain.work.port.WorkUserContextPort;
import com.mc.domain.work.port.WorkUserView;
import com.mc.infrastructure.iam.security.userdetails.IamUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WorkUserViewContextPortImpl implements WorkUserContextPort {

    @Override
    public WorkUserView getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof IamUserDetails details)) {
            throw new UserNotFoundException("No authenticated user found in security context");
        }

        return new WorkUserView(
                details.getUserId(),
                details.getEmail(),
                details.getUser().getProfile().getFullName()
        );
    }

}
