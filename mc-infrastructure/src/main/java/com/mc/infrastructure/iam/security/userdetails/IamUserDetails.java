package com.mc.infrastructure.iam.security.userdetails;

import com.mc.domain.iam.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * IAM UserDetails Implementation - Infrastructure Layer
 * Adapts domain User model to Spring Security UserDetails.
 */
@Getter
@RequiredArgsConstructor
public class IamUserDetails implements UserDetails {

    private final User user;

    public UUID getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmailValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmailValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
