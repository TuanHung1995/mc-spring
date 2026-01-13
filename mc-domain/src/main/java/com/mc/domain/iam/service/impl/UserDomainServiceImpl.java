package com.mc.domain.iam.service.impl;

import com.mc.domain.iam.exception.InvalidCredentialsException;
import com.mc.domain.iam.exception.UserAlreadyExistsException;
import com.mc.domain.iam.exception.UserNotFoundException;
import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.UserRepository;
import com.mc.domain.iam.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Domain Service Implementation - IAM Bounded Context
 * Contains domain logic that orchestrates User aggregate operations.
 */
@Service("iamUserDomainService")
@RequiredArgsConstructor
@Transactional
public class UserDomainServiceImpl implements UserDomainService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerWithEmail(String email, String rawPassword, String fullName) {
        Email emailVO = new Email(email);
        
        // Check if user already exists
        if (userRepository.existsByEmail(emailVO)) {
            throw UserAlreadyExistsException.withEmail(email);
        }
        
        // Create and save user
        String passwordHash = passwordEncoder.encode(rawPassword);
        User user = User.registerWithEmail(emailVO, passwordHash, fullName);
        
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateProfile(UUID userId, String fullName, String avatarUrl, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));
        
        user.updateProfile(fullName, avatarUrl, bio);
        
        return userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        
        // Change password
        String newPasswordHash = passwordEncoder.encode(newPassword);
        user.changePassword(newPasswordHash);
        
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword, UUID excludeUserId) {
        // TODO: Implement search in repository
        return List.of();
    }
}
