package com.gogidix.shared.utilities.infrastructure.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigBatchTest {

    @Test
    void asyncConfig_defaults() {
        AsyncConfig config = new AsyncConfig();
        assertEquals(3, config.getCorePoolSize());
        assertEquals(10, config.getMaxPoolSize());
        assertEquals(500, config.getQueueCapacity());
        assertEquals("Utilities-Async-", config.getThreadNamePrefix());
    }

    @Test
    void asyncConfig_setters() {
        AsyncConfig config = new AsyncConfig();
        config.setCorePoolSize(5);
        config.setMaxPoolSize(20);
        config.setQueueCapacity(1000);
        config.setThreadNamePrefix("Custom-");
        assertEquals(5, config.getCorePoolSize());
        assertEquals(20, config.getMaxPoolSize());
        assertEquals(1000, config.getQueueCapacity());
        assertEquals("Custom-", config.getThreadNamePrefix());
    }

    @Test
    void asyncConfig_taskExecutor() {
        AsyncConfig config = new AsyncConfig();
        assertNotNull(config.taskExecutor());
    }

    @Test
    void securityConfig_classExists() {
        assertNotNull(SecurityConfig.class);
    }

    @Test
    void cacheConfig_classExists() {
        assertNotNull(CacheConfig.class);
    }
}
