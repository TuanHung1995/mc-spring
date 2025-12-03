package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.TokenBlacklist;
import com.mc.domain.repository.TokenBlacklistRepository;
import com.mc.infrastructure.persistence.mapper.TokenBlacklistJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistInfrasRepository implements TokenBlacklistRepository {

    private final TokenBlacklistJPAMapper tokenBlacklistJPAMapper;

    @Override
    public boolean existsByToken(String token) {
        return tokenBlacklistJPAMapper.existsByToken(token);
    }

    @Override
    public void save(TokenBlacklist tokenBlacklist) {
        tokenBlacklistJPAMapper.save(tokenBlacklist);
    }

}
