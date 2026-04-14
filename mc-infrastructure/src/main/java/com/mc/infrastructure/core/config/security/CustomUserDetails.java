package com.mc.infrastructure.core.config.security;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user.getTeamMembers().stream()
//                .map(teamMember -> new SimpleGrantedAuthority(
//                        teamMember.getRole().getName() + "_TEAM_" + teamMember.getTeam().getId()
//                ))
//                .collect(Collectors.toList());
        return null;
    }

    public UUID getUserId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail().getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != AccountStatus.LOCKED;
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
