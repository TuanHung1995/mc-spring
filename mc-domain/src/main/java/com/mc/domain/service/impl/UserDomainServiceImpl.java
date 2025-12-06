package com.mc.domain.service.impl;

import com.mc.domain.exception.ResourceNotFoundException;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.UserRepository;
import com.mc.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;

    @Override
    public User updateProfile(Long userId, String fullName, String phone, String address, String jobTitle, String birthday) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Logic "Partial Update": Chỉ update nếu có dữ liệu mới
        if (StringUtils.hasText(fullName)) {
            user.setFullName(fullName);
        }

        if (StringUtils.hasText(phone)) {
            user.setPhone(phone);
        }

        if (StringUtils.hasText(address)) {
            user.setAddress(address);
        }

        if (StringUtils.hasText(jobTitle)) {
            user.setJobTitle(jobTitle);
        }

        if (StringUtils.hasText(birthday)) {
            // check date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(birthday, formatter);
            user.setBirthday(Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        }

        return userRepository.saveUser(user);

    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

}
