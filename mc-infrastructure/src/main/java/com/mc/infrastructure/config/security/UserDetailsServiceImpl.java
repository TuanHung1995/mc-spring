package com.mc.infrastructure.config.security;

import com.mc.domain.model.entity.User;
import com.mc.domain.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Ép buộc tải teamMembers và role bên trong nó
        // Vì getAuthorities() cần truy cập vào teamMembers -> role
        Hibernate.initialize(user.getTeamMembers());

        // Nếu cần thiết, có thể loop qua để chắc chắn load hết
        user.getTeamMembers().forEach(member -> Hibernate.initialize(member.getRole()));

        System.out.println("User found: " + user.getEmail());
        return new CustomUserDetails(user);
    }
}
