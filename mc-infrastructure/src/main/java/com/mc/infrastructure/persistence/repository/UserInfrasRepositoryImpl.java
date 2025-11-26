package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.User;
import com.mc.domain.model.enums.AccountStatus;
import com.mc.domain.port.MailSender;
import com.mc.domain.repository.UserRepository;
import com.mc.infrastructure.persistence.mapper.UserJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserInfrasRepositoryImpl implements UserRepository {

    @Value("${server.port}")
    private String serverPort;

    @Value("${constants.auth-api-url}")
    private String authApiUrl;

    @Value("${constants.development}")
    private boolean isDevelopment;

    @Value("${constants.app-url}")
    private String appUrl;

    private final UserJPAMapper userJPAMapper;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public UserInfrasRepositoryImpl(UserJPAMapper userJPAMapper, MailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userJPAMapper = userJPAMapper;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
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
        user.setStatus(AccountStatus.ACTIVE.name());
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

    @Override
    public void forgotPassword(String email) {

        User user = userJPAMapper.findByEmail(email)
                .orElse(null);

        if (user == null) {
            log.warn("User with email {} not found for password reset", email);
            return;
        }

        String token = UUID.randomUUID().toString();

        // Set reset token for the user
        user.setResetToken(token);
        userJPAMapper.save(user);

        String rootUrl;

        if (isDevelopment) {
            rootUrl = "http://localhost:" + serverPort;
        } else {
            rootUrl = appUrl;
        }

        String link = rootUrl + authApiUrl + "/reset-password?token=" + token;
        String content = "Click on link below to reset your password:\n" + link;

        mailSender.send(user.getEmail(), "Reset Password", content);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Find user by reset token
        User user = userJPAMapper.findByResetToken(token);
        if (user == null) {
            log.warn("Invalid reset token: {}", token);
            return;
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        // Set token to null after reset
        user.setResetToken(null);
        userJPAMapper.save(user);
    }

    @Override
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        return userJPAMapper.findByProviderAndProviderId(provider, providerId);
    }

}
