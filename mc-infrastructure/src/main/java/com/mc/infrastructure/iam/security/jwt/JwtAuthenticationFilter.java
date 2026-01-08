package com.mc.infrastructure.iam.security.jwt;

import com.mc.domain.iam.repository.TokenBlacklistRepository;
import com.mc.infrastructure.constant.SecurityConstants;
import com.mc.infrastructure.iam.security.userdetails.IamUserDetailsService;
import com.mc.infrastructure.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter - IAM Infrastructure
 * Intercepts requests and authenticates users based on JWT tokens.
 */
@Component("iamJwtAuthenticationFilter")
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    
    @Qualifier("iamUserDetailsService")
    private final IamUserDetailsService iamUserDetailsService;
    
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final CookieUtils cookieUtils;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, IamUserDetailsService iamUserDetailsService, TokenBlacklistRepository tokenBlacklistRepository, CookieUtils cookieUtils) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.iamUserDetailsService = iamUserDetailsService;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.cookieUtils = cookieUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        String token = getJwtFromRequest(request);
        
        // Check if token is blacklisted
        if (StringUtils.hasText(token) && tokenBlacklistRepository.existsByToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // Validate and authenticate
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = iamUserDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // Try Authorization header first
        String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }

        // Fall back to cookie
        return cookieUtils.getCookieValue(request, CookieUtils.ACCESS_TOKEN_COOKIE_NAME);
    }
}
