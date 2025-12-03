package com.mc.infrastructure.config.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String BOARD_ROLE_CACHE = "boardRoles";
    public static final String TEAM_ROLE_CACHE = "teamRoles";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(BOARD_ROLE_CACHE, TEAM_ROLE_CACHE);

        // Cấu hình: Hết hạn sau 10 phút kể từ lần ghi cuối cùng, tối đa 10,000 bản ghi
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000));

        return cacheManager;
    }
}
