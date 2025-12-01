package com.mc.domain.repository;

import com.mc.domain.model.entity.TokenBlacklist;

public interface TokenBlacklistRepository {

    boolean existsByToken(String token);
    void save(TokenBlacklist tokenBlacklist);

}
