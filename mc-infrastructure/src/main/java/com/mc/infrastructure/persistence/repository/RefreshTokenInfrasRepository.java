package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.RefreshToken;
import com.mc.domain.model.entity.User;
import com.mc.domain.repository.RefreshTokenRepository;
import com.mc.infrastructure.persistence.mapper.RefreshTokenJPAMapper;
import com.mc.infrastructure.persistence.mapper.UserJPAMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenInfrasRepository implements RefreshTokenRepository {

    private final RefreshTokenJPAMapper refreshTokenJPAMapper;
    private final UserJPAMapper userJPAMapper;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJPAMapper.findByToken(token);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJPAMapper.save(refreshToken);
    }

    @Override
    @Transactional
    public int deleteByUserEmail(String email) {
        Optional<User> user = userJPAMapper.findByEmail(email);
        return user.map(refreshTokenJPAMapper::deleteByUser).orElse(0);
    }

}
