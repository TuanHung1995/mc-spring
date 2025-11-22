package com.mc.domain.service.impl;

import com.mc.domain.model.entity.User;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.AuthDomainService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthDomainServiceImpl implements AuthDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDomainServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User register(String fullName, String email, String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        return userRepository.save(
                email,
                passwordEncoder.encode(password),
                fullName
        );
    }
}
