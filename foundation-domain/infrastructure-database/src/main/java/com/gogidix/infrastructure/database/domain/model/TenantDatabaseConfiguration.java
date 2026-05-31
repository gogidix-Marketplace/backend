package com.gogidix.infrastructure.database.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model representing a tenant's database configuration.
 *
 * <p>Supports multiple multi-tenancy strategies:</p>
 * <ul>
 *   <li>Database per tenant - each tenant has their own database</li>
 *   <li>Schema per tenant - each tenant has their own schema</li>
 *   <li>Shared database - all tenants share the same database with discriminator</li>
 * </ul>
 *
 * <p>Provides tenant isolation, routing, and management capabilities.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tenant_database_configurations")
public class TenantDatabaseConfiguration {

    /**
     * Unique identifier for the tenant database configuration.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Tenant name for display purposes.
     */
    @NotBlank(message = "Tenant name is required")
    private String tenantName;

    /**
     * Environment for this configuration (dev, staging, prod).
     */
    @Indexed
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Multi-tenancy strategy.
     */
    @NotNull(message = "Multi-tenancy strategy is required")
    private TenancyStrategy strategy;

    /**
     * Database type.
     */
    @NotNull(message = "Database type is required")
    private ConnectionPoolConfiguration.DatabaseType databaseType;

    /**
     * Connection pool name to use.
     */
    @Indexed
    private String connectionPoolName;

    /**
     * Database name for database-per-tenant strategy.
     */
    private String databaseName;

    /**
     * Schema name for schema-per-tenant strategy.
     */
    private String schemaName;

    /**
     * Table prefix for shared-database strategy.
     */
    private String tablePrefix;

    /**
     * Discriminator column name for shared-database strategy.
     */
    private String discriminatorColumn;

    /**
     * Discriminator value for this tenant.
     */
    private String discriminatorValue;

    /**
     * JDBC URL template with placeholders.
     */
    private String jdbcUrlTemplate;

    /**
     * Actual JDBC URL for this tenant.
     */
    private String jdbcUrl;

    /**
     * Database username.
     */
    private String username;

    /**
     * Encrypted database password.
     */
    private String encryptedPassword;

    /**
     * Whether this tenant database is active.
     */
    @Indexed
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Tenant database status.
     */
    @Builder.Default
    private TenantStatus status = TenantStatus.PROVISIONING;

    /**
     * Maximum number of connections allowed for this tenant.
     */
    @Builder.Default
    private Integer maxConnections = 10;

    /**
     * Current number of active connections.
     */
    @Builder.Default
    private Integer currentConnections = 0;

    /**
     * Tenant tier (affects resource allocation).
     */
    @Builder.Default
    private TenantTier tier = TenantTier.STANDARD;

    /**
     * Resource quotas for this tenant.
     */
    private ResourceQuota resourceQuota;

    /**
     * Replication configuration.
     */
    private ReplicationConfig replicationConfig;

    /**
     * Backup configuration.
     */
    private BackupConfig backupConfig;

    /**
     * Tags for categorization.
     */
    private Set<String> tags;

    /**
     * Additional properties.
     */
    private Map<String, String> properties;

    /**
     * Owner/team responsible for this tenant.
     */
    private String owner;

    /**
     * User who created this configuration.
     */
    private String createdBy;

    /**
     * User who last updated this configuration.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Metadata associated with this configuration.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when this configuration was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this configuration was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Timestamp until which this configuration is valid.
     */
    private LocalDateTime validUntil;

    /**
     * Provisioning start time.
     */
    private LocalDateTime provisioningStartTime;

    /**
     * Provisioning completion time.
     */
    private LocalDateTime provisioningCompletedAt;

    /**
     * Deprovisioning time.
     */
    private LocalDateTime deprovisionedAt;

    /**
     * Environment enumeration.
     */
    public enum Environment {
        DEV,
        STAGING,
        PROD,
        TEST
    }

    /**
     * Multi-tenancy strategy enumeration.
     */
    public enum TenancyStrategy {
        DATABASE_PER_TENANT,
        SCHEMA_PER_TENANT,
        SHARED_DATABASE,
        HYBRID
    }

    /**
     * Tenant status enumeration.
     */
    public enum TenantStatus {
        PROVISIONING,
        ACTIVE,
        SUSPENDED,
        MAINTENANCE,
        DEPROVISIONING,
        DEPROVISIONED,
        ERROR
    }

    /**
     * Tenant tier enumeration.
     */
    public enum TenantTier {
        BASIC(5, 1L, 5L),
        STANDARD(10, 2L, 10L),
        PREMIUM(50, 5L, 50L),
        ENTERPRISE(200, 10L, 200L);

        private final int maxConnections;
        private final long maxStorageGB;
        private final long maxIOPS;

        TenantTier(int maxConnections, long maxStorageGB, long maxIOPS) {
            this.maxConnections = maxConnections;
            this.maxStorageGB = maxStorageGB;
            this.maxIOPS = maxIOPS;
        }

        public int getMaxConnections() {
            return maxConnections;
        }

        public long getMaxStorageGB() {
            return maxStorageGB;
        }

        public long getMaxIOPS() {
            return maxIOPS;
        }
    }

    /**
     * Resource quota configuration.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceQuota {
        @Builder.Default
        private Integer maxConnections = 10;

        @Builder.Default
        private Long maxStorageGB = 10L;

        @Builder.Default
        private Long maxQueryExecutionTimeMs = 30000L;

        @Builder.Default
        private Integer maxConcurrentQueries = 5;

        @Builder.Default
        private Long maxRowsReturned = 100000L;

        private Long maxTemporarySpaceGB;
        private Long maxTransactionDurationMs;
    }

    /**
     * Replication configuration.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplicationConfig {
        @Builder.Default
        private Boolean enabled = false;

        private Integer replicaCount;
        private String replicationMode;
        private String primaryDatabase;
        private Set<String> replicaDatabases;
        private Boolean readFromReplicas;
        private Integer replicationLagSeconds;
    }

    /**
     * Backup configuration.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackupConfig {
        @Builder.Default
        private Boolean enabled = true;

        @Builder.Default
        private Integer retentionDays = 30;

        private String backupSchedule;
        @Builder.Default
        private Boolean compressionEnabled = true;

        private String backupLocation;
        private Set<String> backupTypes;
    }

    /**
     * Validates the tenant database configuration.
     */
    public boolean isValid() {
        if (tenantId == null || tenantId.isEmpty()) {
            return false;
        }

        if (strategy == null) {
            return false;
        }

        switch (strategy) {
            case DATABASE_PER_TENANT:
                return databaseName != null && !databaseName.isEmpty();
            case SCHEMA_PER_TENANT:
                return schemaName != null && !schemaName.isEmpty();
            case SHARED_DATABASE:
                return discriminatorColumn != null && discriminatorValue != null;
            case HYBRID:
                return databaseName != null || schemaName != null;
            default:
                return false;
        }
    }

    /**
     * Checks if tenant can accept new connections.
     */
    public boolean canAcceptConnection() {
        return isActive != null && isActive
                && status == TenantStatus.ACTIVE
                && (currentConnections == null || currentConnections < maxConnections);
    }

    /**
     * Gets effective max connections based on tier and custom config.
     */
    public int getEffectiveMaxConnections() {
        if (resourceQuota != null && resourceQuota.getMaxConnections() != null) {
            return Math.min(resourceQuota.getMaxConnections(), tier.getMaxConnections());
        }
        return tier.getMaxConnections();
    }

    /**
     * Gets the effective JDBC URL for this tenant.
     */
    public String getEffectiveJdbcUrl() {
        return jdbcUrl != null ? jdbcUrl : jdbcUrlTemplate;
    }
}
