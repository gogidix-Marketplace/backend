package com.gogidix.shared.utilities.infrastructure.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AsyncConfigTest {

    @Test
    void gettersAndSetters() {
        AsyncConfig config = new AsyncConfig();
        assertEquals(3, config.getCorePoolSize());
        assertEquals(10, config.getMaxPoolSize());
        assertEquals(500, config.getQueueCapacity());
        assertEquals("Utilities-Async-", config.getThreadNamePrefix());

        config.setCorePoolSize(5);
        assertEquals(5, config.getCorePoolSize());

        config.setMaxPoolSize(20);
        assertEquals(20, config.getMaxPoolSize());

        config.setQueueCapacity(1000);
        assertEquals(1000, config.getQueueCapacity());

        config.setThreadNamePrefix("Test-");
        assertEquals("Test-", config.getThreadNamePrefix());
    }

    @Test
    void taskExecutor() {
        AsyncConfig config = new AsyncConfig();
        java.util.concurrent.Executor executor = config.taskExecutor();
        assertNotNull(executor);
    }
}
