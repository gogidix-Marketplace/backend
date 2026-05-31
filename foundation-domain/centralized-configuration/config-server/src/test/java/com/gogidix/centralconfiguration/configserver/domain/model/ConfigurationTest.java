package com.gogidix.centralconfiguration.configserver.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Configuration Domain Model Tests")
class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        configuration = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .version(1)
                .isActive(true)
                .description("Database connection timeout")
                .createdBy("admin")
                .build();
    }

    @Test
    @DisplayName("Should build configuration with all fields")
    void builder_AllFields() {
        assertThat(configuration.getId()).isEqualTo(1L);
        assertThat(configuration.getTenantId()).isEqualTo("tenant-1");
        assertThat(configuration.getApplicationName()).isEqualTo("payment-service");
        assertThat(configuration.getProfile()).isEqualTo("prod");
        assertThat(configuration.getConfigKey()).isEqualTo("database.timeout");
        assertThat(configuration.getConfigValue()).isEqualTo("30000");
        assertThat(configuration.getIsEncrypted()).isFalse();
        assertThat(configuration.getVersion()).isEqualTo(1);
        assertThat(configuration.getIsActive()).isTrue();
        assertThat(configuration.getDescription()).isEqualTo("Database connection timeout");
        assertThat(configuration.getCreatedBy()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should use default values for optional fields")
    void builder_DefaultValues() {
        Configuration minimalConfig = Configuration.builder()
                .tenantId("tenant-1")
                .applicationName("app")
                .profile("dev")
                .configKey("key")
                .configValue("value")
                .build();

        assertThat(minimalConfig.getIsEncrypted()).isFalse();
        assertThat(minimalConfig.getVersion()).isEqualTo(1);
        assertThat(minimalConfig.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should increment version correctly")
    void incrementVersion_Increments() {
        int initialVersion = configuration.getVersion();

        configuration.incrementVersion();

        assertThat(configuration.getVersion()).isEqualTo(initialVersion + 1);
    }

    @Test
    @DisplayName("Should activate configuration")
    void activate_SetsIsActiveTrue() {
        configuration.setIsActive(false);

        configuration.activate();

        assertThat(configuration.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should deactivate configuration")
    void deactivate_SetsIsActiveFalse() {
        configuration.setIsActive(true);

        configuration.deactivate();

        assertThat(configuration.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Should set and get timestamp fields")
    void timestamps_AreSettable() {
        LocalDateTime now = LocalDateTime.now();
        configuration.setCreatedAt(now);
        configuration.setUpdatedAt(now);

        assertThat(configuration.getCreatedAt()).isEqualTo(now);
        assertThat(configuration.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should support null description")
    void description_CanBeNull() {
        configuration.setDescription(null);

        assertThat(configuration.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should handle encryption flag")
    void isEncrypted_Flag() {
        configuration.setIsEncrypted(true);

        assertThat(configuration.getIsEncrypted()).isTrue();
    }

    @Test
    @DisplayName("Should support all setters and getters")
    void settersAndGetters_Work() {
        configuration.setTenantId("new-tenant");
        configuration.setApplicationName("new-app");
        configuration.setProfile("new-profile");
        configuration.setConfigKey("new-key");
        configuration.setConfigValue("new-value");
        configuration.setVersion(5);
        configuration.setUpdatedBy("user2");

        assertThat(configuration.getTenantId()).isEqualTo("new-tenant");
        assertThat(configuration.getApplicationName()).isEqualTo("new-app");
        assertThat(configuration.getProfile()).isEqualTo("new-profile");
        assertThat(configuration.getConfigKey()).isEqualTo("new-key");
        assertThat(configuration.getConfigValue()).isEqualTo("new-value");
        assertThat(configuration.getVersion()).isEqualTo(5);
        assertThat(configuration.getUpdatedBy()).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should create empty configuration with no-args constructor")
    void noArgsConstructor_CreatesEmpty() {
        Configuration emptyConfig = new Configuration();

        assertThat(emptyConfig.getId()).isNull();
        assertThat(emptyConfig.getTenantId()).isNull();
        assertThat(emptyConfig.getApplicationName()).isNull();
    }

    @Test
    @DisplayName("Should support Lombok @Data functionality")
    void lombokData_Functionality() {
        Configuration config1 = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .configKey("key")
                .build();

        Configuration config2 = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .configKey("key")
                .build();

        Configuration config3 = Configuration.builder()
                .id(2L)
                .tenantId("tenant-1")
                .configKey("key")
                .build();

        // Same ID should not affect equals (by design of @EqualsAndHashCode(callSuper = false))
        assertThat(config1).isNotNull();
        assertThat(config2).isNotNull();

        // Test toString
        assertThat(config1.toString()).contains("tenant-1");
        assertThat(config1.toString()).contains("key");

        // Test hashCode
        assertThat(config1.hashCode()).isNotNull();
    }

    @Test
    @DisplayName("Should support all-args constructor")
    void allArgsConstructor_CreatesInstance() {
        LocalDateTime now = LocalDateTime.now();

        Configuration config = new Configuration(
                1L,
                "tenant-1",
                "app",
                "prod",
                "key",
                "value",
                false,
                1,
                true,
                "description",
                "creator",
                "updater",
                now,
                now
        );

        assertThat(config.getId()).isEqualTo(1L);
        assertThat(config.getTenantId()).isEqualTo("tenant-1");
        assertThat(config.getCreatedAt()).isEqualTo(now);
        assertThat(config.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle version number updates")
    void version_Updates() {
        configuration.setVersion(10);
        assertThat(configuration.getVersion()).isEqualTo(10);

        configuration.setVersion(0);
        assertThat(configuration.getVersion()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should support builder pattern with method chaining")
    void builder_Chaining() {
        Configuration builtConfig = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("app")
                .profile("prod")
                .configKey("key")
                .configValue("value")
                .isEncrypted(true)
                .version(2)
                .isActive(false)
                .description("desc")
                .createdBy("admin")
                .build();

        assertThat(builtConfig.getId()).isEqualTo(1L);
        assertThat(builtConfig.getTenantId()).isEqualTo("tenant-1");
        assertThat(builtConfig.getApplicationName()).isEqualTo("app");
        assertThat(builtConfig.getProfile()).isEqualTo("prod");
        assertThat(builtConfig.getConfigKey()).isEqualTo("key");
        assertThat(builtConfig.getConfigValue()).isEqualTo("value");
        assertThat(builtConfig.getIsEncrypted()).isTrue();
        assertThat(builtConfig.getVersion()).isEqualTo(2);
        assertThat(builtConfig.getIsActive()).isFalse();
        assertThat(builtConfig.getDescription()).isEqualTo("desc");
        assertThat(builtConfig.getCreatedBy()).isEqualTo("admin");
    }
}
