package com.gogidix.analytics.metrics.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisConfigTest {

    @Test
    void cacheManager_createsSuccessfully() {
        RedisConfig config = new RedisConfig();
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);

        RedisCacheManager manager = config.cacheManager(factory);

        assertNotNull(manager);
    }
}
