package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.ConnectionPoolConfiguration;
import com.gogidix.infrastructure.database.domain.repository.ConnectionPoolConfigurationRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing database connection pools.
 *
 * <p>Provides connection pool lifecycle management, health monitoring,
 * and dynamic configuration updates.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionPoolService {

    private final ConnectionPoolConfigurationRepository repository;

    private final Map<String, HikariDataSource> activeDataSources = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        log.info("Initializing ConnectionPoolService");
        loadActivePools();
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down ConnectionPoolService, closing {} data sources", activeDataSources.size());
        activeDataSources.forEach((key, ds) -> {
            try {
                if (!ds.isClosed()) {
                    ds.close();
                    log.debug("Closed data source: {}", key);
                }
            } catch (Exception e) {
                log.warn("Error closing data source {}: {}", key, e.getMessage());
            }
        });
        activeDataSources.clear();
    }

    /**
     * Load and initialize all active connection pools from repository.
     */
    private void loadActivePools() {
        List<ConnectionPoolConfiguration> activePools = repository.findAllByIsActive(true);
        log.info("Loading {} active connection pools", activePools.size());

        for (ConnectionPoolConfiguration config : activePools) {
            try {
                if (config.isValid()) {
                    initializePool(config);
                } else {
                    log.warn("Skipping invalid pool configuration: {}", config.getPoolName());
                }
            } catch (Exception e) {
                log.error("Failed to initialize pool {}: {}", config.getPoolName(), e.getMessage());
            }
        }
    }

    /**
     * Create a new connection pool configuration.
     */
    @Transactional
    @CacheEvict(value = "connectionPools", allEntries = true)
    public ConnectionPoolConfiguration createPool(@Valid ConnectionPoolConfiguration configuration) {
        log.info("Creating connection pool: {}", configuration.getPoolName());

        if (!configuration.isValid()) {
            throw new IllegalArgumentException("Invalid connection pool configuration");
        }

        if (repository.existsByTenantIdAndPoolName(
                configuration.getTenantId(),
                configuration.getPoolName())) {
            throw new IllegalArgumentException(
                    "Connection pool already exists: " + configuration.getPoolName());
        }

        ConnectionPoolConfiguration saved = repository.save(configuration);

        if (saved.getIsActive()) {
            try {
                initializePool(saved);
            } catch (Exception e) {
                log.error("Failed to initialize newly created pool: {}", e.getMessage());
            }
        }

        return saved;
    }

    /**
     * Initialize a connection pool from configuration.
     */
    private HikariDataSource initializePool(ConnectionPoolConfiguration configuration) {
        log.info("Initializing connection pool: {}", configuration.getPoolName());

        String key = getPoolKey(configuration.getTenantId(), configuration.getPoolName());

        if (activeDataSources.containsKey(key)) {
            closePool(key);
        }

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(configuration.getPoolName());
        hikariConfig.setJdbcUrl(configuration.getJdbcUrl());
        hikariConfig.setUsername(configuration.getUsername());
        hikariConfig.setPassword(decryptPassword(configuration.getEncryptedPassword()));
        hikariConfig.setDriverClassName(configuration.getEffectiveDriverClassName());
        hikariConfig.setMinimumIdle(configuration.getMinimumIdle());
        hikariConfig.setMaximumPoolSize(configuration.getMaximumPoolSize());
        hikariConfig.setConnectionTimeout(configuration.getConnectionTimeout());
        hikariConfig.setIdleTimeout(configuration.getIdleTimeout());
        hikariConfig.setMaxLifetime(configuration.getMaxLifetime());
        hikariConfig.setLeakDetectionThreshold(configuration.getLeakDetectionThreshold());

        if (configuration.getConnectionTestQuery() != null) {
            hikariConfig.setConnectionTestQuery(configuration.getConnectionTestQuery());
        }

        if (configuration.getConnectionInitSql() != null) {
            hikariConfig.setConnectionInitSql(configuration.getConnectionInitSql());
        }

        if (configuration.getValidationTimeout() != null) {
            hikariConfig.setValidationTimeout(configuration.getValidationTimeout());
        }

        if (configuration.getReadOnly() != null) {
            hikariConfig.setReadOnly(configuration.getReadOnly());
        }

        if (configuration.getAllowPoolSuspension() != null) {
            hikariConfig.setAllowPoolSuspension(configuration.getAllowPoolSuspension());
        }

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        activeDataSources.put(key, dataSource);

        // Update status
        configuration.setStatus(ConnectionPoolConfiguration.PoolStatus.ACTIVE);
        repository.save(configuration);

        log.info("Connection pool initialized successfully: {}", configuration.getPoolName());
        return dataSource;
    }

    /**
     * Get connection pool configuration by tenant and pool name.
     */
    @Cacheable(value = "connectionPools", key = "#tenantId + ':' + #poolName")
    public Optional<ConnectionPoolConfiguration> getPool(String tenantId, String poolName) {
        return repository.findByTenantIdAndPoolName(tenantId, poolName);
    }

    /**
     * Get all connection pools for a tenant.
     */
    public List<ConnectionPoolConfiguration> getPoolsByTenant(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    /**
     * Get active data source for a pool.
     */
    public Optional<HikariDataSource> getDataSource(String tenantId, String poolName) {
        String key = getPoolKey(tenantId, poolName);
        return Optional.ofNullable(activeDataSources.get(key));
    }

    /**
     * Update connection pool configuration.
     */
    @Transactional
    @CacheEvict(value = "connectionPools", allEntries = true)
    public ConnectionPoolConfiguration updatePool(String id, @Valid ConnectionPoolConfiguration configuration) {
        log.info("Updating connection pool: {}", configuration.getPoolName());

        ConnectionPoolConfiguration existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connection pool not found: " + id));

        if (!existing.getTenantId().equals(configuration.getTenantId())
                || !existing.getPoolName().equals(configuration.getPoolName())) {
            throw new IllegalArgumentException("Cannot change tenant ID or pool name");
        }

        configuration.setId(id);
        configuration.setVersion(existing.getVersion() + 1);
        configuration.setCreatedAt(existing.getCreatedAt());

        ConnectionPoolConfiguration saved = repository.save(configuration);

        // Reinitialize pool if active
        if (saved.getIsActive()) {
            try {
                initializePool(saved);
            } catch (Exception e) {
                log.error("Failed to reinitialize pool after update: {}", e.getMessage());
            }
        }

        return saved;
    }

    /**
     * Delete connection pool configuration.
     */
    @Transactional
    @CacheEvict(value = "connectionPools", allEntries = true)
    public void deletePool(String tenantId, String poolName) {
        log.info("Deleting connection pool: {}", poolName);

        ConnectionPoolConfiguration configuration = repository.findByTenantIdAndPoolName(tenantId, poolName)
                .orElseThrow(() -> new IllegalArgumentException("Connection pool not found: " + poolName));

        String key = getPoolKey(tenantId, poolName);
        closePool(key);

        repository.delete(configuration);
        log.info("Connection pool deleted: {}", poolName);
    }

    /**
     * Close a connection pool.
     */
    private void closePool(String key) {
        HikariDataSource dataSource = activeDataSources.remove(key);
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("Closed connection pool: {}", key);
        }
    }

    /**
     * Activate a connection pool.
     */
    @Transactional
    @CacheEvict(value = "connectionPools", allEntries = true)
    public ConnectionPoolConfiguration activatePool(String tenantId, String poolName) {
        ConnectionPoolConfiguration configuration = repository.findByTenantIdAndPoolName(tenantId, poolName)
                .orElseThrow(() -> new IllegalArgumentException("Connection pool not found: " + poolName));

        configuration.setIsActive(true);
        ConnectionPoolConfiguration saved = repository.save(configuration);

        initializePool(saved);
        return saved;
    }

    /**
     * Deactivate a connection pool.
     */
    @Transactional
    @CacheEvict(value = "connectionPools", allEntries = true)
    public ConnectionPoolConfiguration deactivatePool(String tenantId, String poolName) {
        ConnectionPoolConfiguration configuration = repository.findByTenantIdAndPoolName(tenantId, poolName)
                .orElseThrow(() -> new IllegalArgumentException("Connection pool not found: " + poolName));

        configuration.setIsActive(false);
        ConnectionPoolConfiguration saved = repository.save(configuration);

        String key = getPoolKey(tenantId, poolName);
        closePool(key);

        return saved;
    }

    /**
     * Get pool statistics.
     */
    public Map<String, Object> getPoolStatistics(String tenantId, String poolName) {
        String key = getPoolKey(tenantId, poolName);
        HikariDataSource dataSource = activeDataSources.get(key);

        if (dataSource == null || dataSource.isClosed()) {
            return Collections.emptyMap();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeConnections", dataSource.getHikariPoolMXBean().getActiveConnections());
        stats.put("idleConnections", dataSource.getHikariPoolMXBean().getIdleConnections());
        stats.put("totalConnections", dataSource.getHikariPoolMXBean().getTotalConnections());
        stats.put("threadsAwaitingConnection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        stats.put("isClosed", dataSource.isClosed());

        return stats;
    }

    /**
     * Get all pool statistics.
     */
    public Map<String, Map<String, Object>> getAllPoolStatistics() {
        Map<String, Map<String, Object>> allStats = new HashMap<>();

        activeDataSources.forEach((key, ds) -> {
            if (!ds.isClosed()) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("activeConnections", ds.getHikariPoolMXBean().getActiveConnections());
                stats.put("idleConnections", ds.getHikariPoolMXBean().getIdleConnections());
                stats.put("totalConnections", ds.getHikariPoolMXBean().getTotalConnections());
                stats.put("threadsAwaitingConnection", ds.getHikariPoolMXBean().getThreadsAwaitingConnection());
                allStats.put(key, stats);
            }
        });

        return allStats;
    }

    /**
     * Health check for all connection pools.
     */
    public Map<String, Boolean> healthCheck() {
        Map<String, Boolean> healthStatus = new HashMap<>();

        activeDataSources.forEach((key, ds) -> {
            boolean healthy = false;
            try {
                if (!ds.isClosed()) {
                    ds.getConnection().close();
                    healthy = true;
                }
            } catch (Exception e) {
                log.warn("Health check failed for pool {}: {}", key, e.getMessage());
            }
            healthStatus.put(key, healthy);
        });

        return healthStatus;
    }

    /**
     * Get pool key for storage.
     */
    private String getPoolKey(String tenantId, String poolName) {
        return tenantId + ":" + poolName;
    }

    /**
     * Decrypt password (placeholder - implement proper decryption).
     */
    private String decryptPassword(String encryptedPassword) {
        // TODO: Implement proper decryption using encryption service
        return encryptedPassword;
    }
}
