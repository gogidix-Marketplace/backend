package com.gogidix.infrastructure.database.domain.model;

import jakarta.validation.constraints.Min;
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
 * Domain model representing a database connection pool configuration.
 *
 * <p>Manages connection pool settings for various database types including:</p>
 * <ul>
 *   <li>PostgreSQL</li>
 *   <li>MySQL</li>
 *   <li>Oracle</li>
 *   <li>MSSQL</li>
 *   <li>H2</li>
 * </ul>
 *
 * <p>Provides configuration for HikariCP connection pooling with tenant awareness.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "connection_pool_configurations")
public class ConnectionPoolConfiguration {

    /**
     * Unique identifier for the connection pool configuration.
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
     * Environment for this configuration (dev, staging, prod).
     */
    @Indexed
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Unique name for this connection pool.
     */
    @Indexed
    @NotBlank(message = "Pool name is required")
    private String poolName;

    /**
     * Human-readable description of the pool.
     */
    private String description;

    /**
     * Database type (POSTGRESQL, MYSQL, ORACLE, MSSQL, H2).
     */
    @NotNull(message = "Database type is required")
    private DatabaseType databaseType;

    /**
     * JDBC URL for the database connection.
     */
    @NotBlank(message = "JDBC URL is required")
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
     * The data source class name.
     */
    private String dataSourceClassName;

    /**
     * The driver class name.
     */
    private String driverClassName;

    /**
     * Minimum number of idle connections in the pool.
     */
    @Builder.Default
    @Min(value = 0, message = "Minimum idle cannot be negative")
    private Integer minimumIdle = 2;

    /**
     * Maximum pool size.
     */
    @Builder.Default
    @Min(value = 1, message = "Maximum pool size must be at least 1")
    private Integer maximumPoolSize = 10;

    /**
     * Connection timeout in milliseconds.
     */
    @Builder.Default
    @Min(value = 100, message = "Connection timeout must be at least 100ms")
    private Long connectionTimeout = 30000L;

    /**
     * Idle timeout for connections in milliseconds.
     */
    @Builder.Default
    @Min(value = 0, message = "Idle timeout cannot be negative")
    private Long idleTimeout = 600000L;

    /**
     * Maximum lifetime of a connection in milliseconds.
     */
    @Builder.Default
    @Min(value = 0, message = "Max lifetime cannot be negative")
    private Long maxLifetime = 1800000L;

    /**
     * Leak detection threshold in milliseconds.
     */
    @Builder.Default
    @Min(value = 0, message = "Leak detection threshold cannot be negative")
    private Long leakDetectionThreshold = 0L;

    /**
     * Maximum time a connection can be out of the pool.
     */
    private Long maxConnectionAge;

    /**
     * Whether to keep connections alive.
     */
    @Builder.Default
    private Boolean keepAlive = true;

    /**
     * Keep alive time in milliseconds.
     */
    private Long keepAliveTime;

    /**
     * Connection test query.
     */
    private String connectionTestQuery;

    /**
     * Connection test timeout in milliseconds.
     */
    private Long connectionTestTimeout;

    /**
     * Validation timeout in milliseconds.
     */
    private Long validationTimeout;

    /**
     * Whether to initialize the pool lazily.
     */
    @Builder.Default
    private Boolean initializePoolLazy = false;

    /**
     * Whether to allow pool suspension.
     */
    @Builder.Default
    private Boolean allowPoolSuspension = false;

    /**
     * Read-only flag.
     */
    @Builder.Default
    private Boolean readOnly = false;

    /**
     * Connection initialization SQL.
     */
    private String connectionInitSql;

    /**
     * Whether this pool is active.
     */
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Tags for categorization.
     */
    private Set<String> tags;

    /**
     * Additional pool properties.
     */
    private Map<String, String> properties;

    /**
     * Health check configuration.
     */
    private HealthCheckConfig healthCheckConfig;

    /**
     * Metrics configuration.
     */
    private MetricsConfig metricsConfig;

    /**
     * Current pool status.
     */
    @Builder.Default
    private PoolStatus status = PoolStatus.CREATED;

    /**
     * Current active connections count.
     */
    @Builder.Default
    private Integer activeConnections = 0;

    /**
     * Current idle connections count.
     */
    @Builder.Default
    private Integer idleConnections = 0;

    /**
     * Total connections count.
     */
    @Builder.Default
    private Integer totalConnections = 0;

    /**
     * Threads awaiting connection count.
     */
    @Builder.Default
    private Integer threadsAwaitingConnection = 0;

    /**
     * Owner/team responsible for this pool.
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
     * Environment enumeration.
     */
    public enum Environment {
        DEV,
        STAGING,
        PROD,
        TEST
    }

    /**
     * Database type enumeration.
     */
    public enum DatabaseType {
        POSTGRESQL("org.postgresql.Driver", "org.postgresql.ds.PGSimpleDataSource"),
        MYSQL("com.mysql.cj.jdbc.Driver", "com.mysql.cj.jdbc.MysqlDataSource"),
        ORACLE("oracle.jdbc.OracleDriver", "oracle.jdbc.pool.OracleDataSource"),
        MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerDataSource"),
        H2("org.h2.Driver", "org.h2.Driver"),
        MARIADB("org.mariadb.jdbc.Driver", "org.mariadb.jdbc.MariaDbDataSource");

        private final String driverClassName;
        private final String dataSourceClassName;

        DatabaseType(String driverClassName, String dataSourceClassName) {
            this.driverClassName = driverClassName;
            this.dataSourceClassName = dataSourceClassName;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public String getDataSourceClassName() {
            return dataSourceClassName;
        }
    }

    /**
     * Pool status enumeration.
     */
    public enum PoolStatus {
        CREATED,
        INITIALIZING,
        ACTIVE,
        SUSPENDED,
        SHUTTING_DOWN,
        SHUTDOWN,
        ERROR
    }

    /**
     * Health check configuration.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthCheckConfig {
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
     * Metrics configuration.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricsConfig {
        @Builder.Default
        private Boolean enabled = true;

        @Builder.Default
        private Boolean trackActiveConnections = true;

        @Builder.Default
        private Boolean trackIdleConnections = true;

        @Builder.Default
        private Boolean trackAwaitingThreads = true;

        @Builder.Default
        private Boolean trackConnectionLatency = true;

        @Builder.Default
        private Long metricsCollectionIntervalMs = 30000L;
    }

    /**
     * Validates the connection pool configuration.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return jdbcUrl != null && !jdbcUrl.isEmpty()
                && databaseType != null
                && minimumIdle >= 0
                && maximumPoolSize > 0
                && minimumIdle <= maximumPoolSize
                && connectionTimeout > 0;
    }

    /**
     * Gets the effective driver class name.
     */
    public String getEffectiveDriverClassName() {
        return driverClassName != null ? driverClassName : databaseType.getDriverClassName();
    }

    /**
     * Gets the effective data source class name.
     */
    public String getEffectiveDataSourceClassName() {
        return dataSourceClassName != null ? dataSourceClassName : databaseType.getDataSourceClassName();
    }
}
