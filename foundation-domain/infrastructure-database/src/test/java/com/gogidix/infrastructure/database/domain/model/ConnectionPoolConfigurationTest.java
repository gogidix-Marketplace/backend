package com.gogidix.infrastructure.database.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConnectionPoolConfiguration.
 */
@DisplayName("ConnectionPoolConfiguration Tests")
class ConnectionPoolConfigurationTest {

    @Test
    @DisplayName("Should create valid connection pool configuration")
    void shouldCreateValidConfiguration() {
        ConnectionPoolConfiguration config = ConnectionPoolConfiguration.builder()
                .tenantId("tenant1")
                .environment(ConnectionPoolConfiguration.Environment.PROD)
                .poolName("main-pool")
                .databaseType(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL)
                .jdbcUrl("jdbc:postgresql://localhost:5432/testdb")
                .username("user")
                .encryptedPassword("encrypted")
                .minimumIdle(2)
                .maximumPoolSize(10)
                .connectionTimeout(30000L)
                .build();

        assertTrue(config.isValid());
        assertEquals("tenant1", config.getTenantId());
        assertEquals(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL, config.getDatabaseType());
    }

    @Test
    @DisplayName("Should validate configuration with invalid settings")
    void shouldValidateInvalidConfiguration() {
        ConnectionPoolConfiguration config = ConnectionPoolConfiguration.builder()
                .tenantId("")
                .poolName("test")
                .databaseType(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL)
                .jdbcUrl("")
                .minimumIdle(20)
                .maximumPoolSize(10)
                .build();

        assertFalse(config.isValid());
    }

    @Test
    @DisplayName("Should get effective driver class name")
    void shouldGetEffectiveDriverClassName() {
        ConnectionPoolConfiguration config = ConnectionPoolConfiguration.builder()
                .databaseType(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL)
                .build();

        assertEquals("org.postgresql.Driver", config.getEffectiveDriverClassName());

        config.setDriverClassName("custom.Driver");
        assertEquals("custom.Driver", config.getEffectiveDriverClassName());
    }

    @Test
    @DisplayName("Should get effective data source class name")
    void shouldGetEffectiveDataSourceClassName() {
        ConnectionPoolConfiguration config = ConnectionPoolConfiguration.builder()
                .databaseType(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL)
                .build();

        assertEquals("org.postgresql.ds.PGSimpleDataSource", config.getEffectiveDataSourceClassName());
    }

    @Test
    @DisplayName("Should create health check config")
    void shouldCreateHealthCheckConfig() {
        ConnectionPoolConfiguration.HealthCheckConfig healthConfig =
                ConnectionPoolConfiguration.HealthCheckConfig.builder()
                        .enabled(true)
                        .intervalMs(60000L)
                        .timeoutMs(5000L)
                        .failureThreshold(3)
                        .healthCheckQuery("SELECT 1")
                        .build();

        assertTrue(healthConfig.getEnabled());
        assertEquals(60000L, healthConfig.getIntervalMs());
        assertEquals("SELECT 1", healthConfig.getHealthCheckQuery());
    }

    @Test
    @DisplayName("Should create metrics config")
    void shouldCreateMetricsConfig() {
        ConnectionPoolConfiguration.MetricsConfig metricsConfig =
                ConnectionPoolConfiguration.MetricsConfig.builder()
                        .enabled(true)
                        .trackActiveConnections(true)
                        .trackIdleConnections(true)
                        .build();

        assertTrue(metricsConfig.getEnabled());
        assertTrue(metricsConfig.getTrackActiveConnections());
    }

    @Test
    @DisplayName("Should handle all database types")
    void shouldHandleAllDatabaseTypes() {
        ConnectionPoolConfiguration.DatabaseType[] types = ConnectionPoolConfiguration.DatabaseType.values();

        assertEquals(6, types.length);

        for (ConnectionPoolConfiguration.DatabaseType type : types) {
            assertNotNull(type.getDriverClassName());
            assertNotNull(type.getDataSourceClassName());
        }
    }

    @Test
    @DisplayName("Should create with all optional fields")
    void shouldCreateWithAllOptionalFields() {
        ConnectionPoolConfiguration config = ConnectionPoolConfiguration.builder()
                .tenantId("tenant1")
                .environment(ConnectionPoolConfiguration.Environment.PROD)
                .poolName("main-pool")
                .description("Main database pool")
                .databaseType(ConnectionPoolConfiguration.DatabaseType.POSTGRESQL)
                .jdbcUrl("jdbc:postgresql://localhost:5432/testdb")
                .username("user")
                .encryptedPassword("encrypted")
                .minimumIdle(2)
                .maximumPoolSize(10)
                .connectionTimeout(30000L)
                .idleTimeout(600000L)
                .maxLifetime(1800000L)
                .leakDetectionThreshold(60000L)
                .connectionTestQuery("SELECT 1")
                .readOnly(false)
                .allowPoolSuspension(false)
                .connectionInitSql("SET TIME ZONE 'UTC'")
                .isActive(true)
                .tags(new HashSet<>())
                .owner("platform-team")
                .createdAt(LocalDateTime.now())
                .build();

        assertTrue(config.isValid());
        assertEquals("Main database pool", config.getDescription());
        assertEquals("platform-team", config.getOwner());
    }
}
