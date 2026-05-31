package com.gogidix.platform.platform.domain;

import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for PlatformConfiguration domain model.
 */
class PlatformConfigurationTest {

    @Test
    void testPlatformConfigurationCreation() {
        PlatformConfiguration config = PlatformConfiguration.builder()
            .tenantId("tenant-1")
            .configKey("test.key")
            .configValue("test-value")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .description("Test configuration")
            .build();

        assertNotNull(config);
        assertEquals("tenant-1", config.getTenantId());
        assertEquals("test.key", config.getConfigKey());
        assertEquals("test-value", config.getConfigValue());
        assertEquals(PlatformConfiguration.ConfigType.STRING, config.getConfigType());
    }

    @Test
    void testIsEffective() {
        PlatformConfiguration config = PlatformConfiguration.builder()
            .tenantId("tenant-1")
            .configKey("test.key")
            .configValue("test-value")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .status(PlatformConfiguration.ConfigStatus.ACTIVE)
            .effectiveFrom(LocalDateTime.now().minusDays(1))
            .effectiveUntil(LocalDateTime.now().plusDays(1))
            .build();

        assertTrue(config.isEffective());
    }

    @Test
    void testIncrementVersion() {
        PlatformConfiguration config = PlatformConfiguration.builder()
            .tenantId("tenant-1")
            .configKey("test.key")
            .configValue("test-value")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .version(1)
            .build();

        config.incrementVersion();
        assertEquals(2, config.getVersion());
    }

    @Test
    void testArchive() {
        PlatformConfiguration config = PlatformConfiguration.builder()
            .tenantId("tenant-1")
            .configKey("test.key")
            .configValue("test-value")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .status(PlatformConfiguration.ConfigStatus.ACTIVE)
            .build();

        config.archive();
        assertEquals(PlatformConfiguration.ConfigStatus.ARCHIVED, config.getStatus());
    }
}
