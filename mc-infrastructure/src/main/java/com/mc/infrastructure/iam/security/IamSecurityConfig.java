package com.mc.infrastructure.iam.security;

import com.mc.infrastructure.iam.security.userdetails.IamUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * IAM Security Configuration - Infrastructure Layer
 * Configures authentication for IAM bounded context.
 */
@Configuration
//@RequiredArgsConstructor
public class IamSecurityConfig {

    @Qualifier("iamUserDetailsService")
    private final IamUserDetailsService iamUserDetailsService;

    public IamSecurityConfig(IamUserDetailsService iamUserDetailsService) {
        this.iamUserDetailsService = iamUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(iamUserDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

}
