package com.gogidix.analytics.bi.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    @Test
    @DisplayName("cacheManager bean is created successfully")
    void cacheManagerBeanIsCreated() {
        RedisConfig config = new RedisConfig();
        RedisCacheManager manager = config.cacheManager(connectionFactory);

        assertNotNull(manager);
    }
}
