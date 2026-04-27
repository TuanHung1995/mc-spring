package com.mc.infrastructure.iam.security.userdetails;

import com.mc.domain.iam.exception.UserNotFoundException;
import com.mc.domain.iam.model.User;
import com.mc.domain.iam.model.vo.Email;
import com.mc.domain.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * IAM UserDetailsService Implementation - Infrastructure Layer
 * Loads user details from IAM domain repository for Spring Security authentication.
 */
@Service("iamUserDetailsService")
//@RequiredArgsConstructor
public class IamUserDetailsService implements UserDetailsService {

    @Qualifier("iamUserRepository")
    private final UserRepository userRepository;

    public IamUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new IamUserDetails(user);
    }
}
