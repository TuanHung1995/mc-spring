package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.User;
import com.mc.domain.repository.UserRepository;
import com.mc.infrastructure.persistence.mapper.UserJPAMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfrasRepositoryImpl implements UserRepository {

    @Value("${server.port}")
    private String serverPort;

    private final UserJPAMapper userJPAMapper;

    public UserInfrasRepositoryImpl(UserJPAMapper userJPAMapper) {
        this.userJPAMapper = userJPAMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJPAMapper.findByEmail(email);
    }

    @Override
    public User save(String email, String password, String fullName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setStatus("ACTIVE");
        return userJPAMapper.save(user);
    }

    @Override
    public void save(User user) {
        userJPAMapper.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJPAMapper.findByEmail(email).isPresent();
    }
}
