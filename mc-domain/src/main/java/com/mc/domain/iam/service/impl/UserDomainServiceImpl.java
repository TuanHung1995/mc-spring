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
    public User updateProfile(User currentUser, String fullName, String avatarUrl, String address, String phone, String jobTitle) {
        currentUser.updateProfile(fullName, avatarUrl, address, phone, jobTitle);
        return userRepository.save(currentUser);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {

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

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String email) {
        Email emailVO = new Email(email);
        return userRepository.findByEmail(emailVO)
                .orElseThrow(() -> UserNotFoundException.withEmail(email));
    }
}
