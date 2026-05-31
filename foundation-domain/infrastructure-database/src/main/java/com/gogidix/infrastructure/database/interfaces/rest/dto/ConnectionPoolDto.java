package com.gogidix.infrastructure.database.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.infrastructure.database.domain.model.ConnectionPoolConfiguration;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * DTO for Connection Pool Configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionPoolDto {

    private String id;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotNull(message = "Environment is required")
    private ConnectionPoolConfiguration.Environment environment;

    @NotBlank(message = "Pool name is required")
    private String poolName;

    private String description;

    @NotNull(message = "Database type is required")
    private ConnectionPoolConfiguration.DatabaseType databaseType;

    @NotBlank(message = "JDBC URL is required")
    private String jdbcUrl;

    private String username;

    private String password; // Plain text for input, will be encrypted

    @Builder.Default
    @Min(value = 0, message = "Minimum idle cannot be negative")
    private Integer minimumIdle = 2;

    @Builder.Default
    @Min(value = 1, message = "Maximum pool size must be at least 1")
    private Integer maximumPoolSize = 10;

    @Builder.Default
    @Min(value = 100, message = "Connection timeout must be at least 100ms")
    private Long connectionTimeout = 30000L;

    @Builder.Default
    @Min(value = 0, message = "Idle timeout cannot be negative")
    private Long idleTimeout = 600000L;

    @Builder.Default
    @Min(value = 0, message = "Max lifetime cannot be negative")
    private Long maxLifetime = 1800000L;

    @Builder.Default
    @Min(value = 0, message = "Leak detection threshold cannot be negative")
    private Long leakDetectionThreshold = 0L;

    private String connectionTestQuery;

    private Boolean readOnly;

    private Boolean allowPoolSuspension;

    private String connectionInitSql;

    @Builder.Default
    private Boolean isActive = true;

    private Set<String> tags;

    private String category;

    private String owner;

    private Map<String, String> properties;

    private HealthCheckConfigDto healthCheckConfig;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime validUntil;

    /**
     * Health check configuration DTO.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthCheckConfigDto {
        @Builder.Default
        private Boolean enabled = true;

        @Builder.Default
        private Long intervalMs = 60000L;

        @Builder.Default
        private Long timeoutMs = 5000L;

        @Builder.Default
        private Integer failureThreshold = 3;

        private String healthCheckQuery;
    }

    /**
     * Convert to domain model.
     */
    public ConnectionPoolConfiguration toDomain() {
        ConnectionPoolConfiguration.ConnectionPoolConfigurationBuilder builder = ConnectionPoolConfiguration.builder()
                .id(id)
                .tenantId(tenantId)
                .environment(environment)
                .poolName(poolName)
                .description(description)
                .databaseType(databaseType)
                .jdbcUrl(jdbcUrl)
                .username(username)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .connectionTimeout(connectionTimeout)
                .idleTimeout(idleTimeout)
                .maxLifetime(maxLifetime)
                .leakDetectionThreshold(leakDetectionThreshold)
                .connectionTestQuery(connectionTestQuery)
                .readOnly(readOnly != null ? readOnly : false)
                .allowPoolSuspension(allowPoolSuspension != null ? allowPoolSuspension : false)
                .connectionInitSql(connectionInitSql)
                .isActive(isActive != null ? isActive : true)
                .tags(tags)
                .owner(owner)
                .properties(properties)
                .validUntil(validUntil);

        if (healthCheckConfig != null) {
            builder.healthCheckConfig(ConnectionPoolConfiguration.HealthCheckConfig.builder()
                    .enabled(healthCheckConfig.getEnabled())
                    .intervalMs(healthCheckConfig.getIntervalMs())
                    .timeoutMs(healthCheckConfig.getTimeoutMs())
                    .failureThreshold(healthCheckConfig.getFailureThreshold())
                    .healthCheckQuery(healthCheckConfig.getHealthCheckQuery())
                    .build());
        }

        return builder.build();
    }

    /**
     * Convert from domain model.
     */
    public static ConnectionPoolDto fromDomain(ConnectionPoolConfiguration config) {
        ConnectionPoolDtoBuilder builder = ConnectionPoolDto.builder()
                .id(config.getId())
                .tenantId(config.getTenantId())
                .environment(config.getEnvironment())
                .poolName(config.getPoolName())
                .description(config.getDescription())
                .databaseType(config.getDatabaseType())
                .jdbcUrl(config.getJdbcUrl())
                .username(config.getUsername())
                .minimumIdle(config.getMinimumIdle())
                .maximumPoolSize(config.getMaximumPoolSize())
                .connectionTimeout(config.getConnectionTimeout())
                .idleTimeout(config.getIdleTimeout())
                .maxLifetime(config.getMaxLifetime())
                .leakDetectionThreshold(config.getLeakDetectionThreshold())
                .connectionTestQuery(config.getConnectionTestQuery())
                .readOnly(config.getReadOnly())
                .allowPoolSuspension(config.getAllowPoolSuspension())
                .connectionInitSql(config.getConnectionInitSql())
                .isActive(config.getIsActive())
                .tags(config.getTags())
                .owner(config.getOwner())
                .properties(config.getProperties())
                .validUntil(config.getValidUntil());

        if (config.getHealthCheckConfig() != null) {
            builder.healthCheckConfig(HealthCheckConfigDto.builder()
                    .enabled(config.getHealthCheckConfig().getEnabled())
                    .intervalMs(config.getHealthCheckConfig().getIntervalMs())
                    .timeoutMs(config.getHealthCheckConfig().getTimeoutMs())
                    .failureThreshold(config.getHealthCheckConfig().getFailureThreshold())
                    .healthCheckQuery(config.getHealthCheckConfig().getHealthCheckQuery())
                    .build());
        }

        return builder.build();
    }
}
