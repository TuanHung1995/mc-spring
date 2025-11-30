package com.mc.domain.repository;

import com.mc.domain.model.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);

    /* Delete refresh token by user email and return number of rows deleted */
    int deleteByUserEmail(String email);

}
