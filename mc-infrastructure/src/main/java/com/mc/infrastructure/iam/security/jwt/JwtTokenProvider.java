package com.mc.infrastructure.iam.security.jwt;

import com.mc.domain.iam.port.TokenHelperPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT Token Provider - IAM Infrastructure
 * Handles JWT token generation and validation for IAM bounded context.
 */
@Component("iamJwtTokenProvider")
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenHelperPort {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.invite.expiration:604800000}")
    private long inviteTokenExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Generate JWT from Spring Security Authentication.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    /**
     * Generate JWT from email (for refresh token flow).
     */
    public String generateTokenFromEmail(String email) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    /**
     * Generate JWT with userId claim (for IAM domain model).
     */
    public String generateTokenWithUserId(String email, UUID userId) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId.toString())
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public UUID getUserIdFromToken(String token) {
        String userIdStr = (String) getAllClaimsFromToken(token).get("userId");
        return userIdStr != null ? UUID.fromString(userIdStr) : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =================================================================
    // INVITE TOKEN (Cross-cutting - used by invitation feature)
    // =================================================================

    /**
     * Generate invite token with boardId and role.
     * TODO: Consider moving to invitation bounded context.
     */
    public String generateInviteToken(String email, Long boardId, String role) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + inviteTokenExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("boardId", boardId)
                .claim("role", role)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    /**
     * Get claims from invite token.
     */
    public Claims getInviteClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
