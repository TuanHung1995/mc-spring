package com.mc.infrastructure.config.security;

import com.mc.domain.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomUserDetails implements UserDetails {

    private final User user;

    private Map<String, Object> attributes;

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Load principal attributes from OAuth2 provider
//    public OAuth2User getOAuth2User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(
                        userRole.getRole().getName() + "_TEAM_" + userRole.getTeam().getId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
