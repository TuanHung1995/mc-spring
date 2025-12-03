package com.mc.infrastructure.persistence.mapper;

import com.mc.domain.model.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistJPAMapper extends JpaRepository<TokenBlacklist, Long> {

    boolean existsByToken(String token);

}
