package com.mc.infrastructure.config.security.context;

import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.port.UserContextPort;
import com.mc.infrastructure.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityUserContext implements UserContextPort {

    @Override
    public Long getCurrentUserId() {
        return findCurrentUserId()
                .orElseThrow(() -> new BusinessLogicException("User not authenticated or context not found"));
    }

    @Override
    public Optional<Long> findCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return Optional.of(((CustomUserDetails) principal).getUserId());
        }

        return Optional.empty();
    }

}
