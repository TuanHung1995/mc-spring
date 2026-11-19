package com.mc.domain.iam.port;

import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.UUID;

public interface TokenHelperPort {

    String generateToken(Authentication authentication);
    String generateTokenFromEmail(String email);
    String generateTokenWithUserId(String email, UUID userId);
    Date getExpirationDateFromToken(String token);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);

}
