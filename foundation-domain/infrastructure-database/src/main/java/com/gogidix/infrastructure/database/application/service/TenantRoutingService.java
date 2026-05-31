package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.TenantDatabaseConfiguration;
import com.gogidix.infrastructure.database.domain.repository.TenantDatabaseConfigurationRepository;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for multi-tenant database routing.
 *
 * <p>Manages tenant-specific database configurations and provides
 * routing for tenant-specific database operations.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TenantRoutingService {

    private final TenantDatabaseConfigurationRepository repository;
    private final ConnectionPoolService connectionPoolService;

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    /**
     * Create a new tenant database configuration.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public TenantDatabaseConfiguration createTenant(@Valid TenantDatabaseConfiguration configuration) {
        log.info("Creating tenant database configuration: {}", configuration.getTenantId());

        if (!configuration.isValid()) {
            throw new IllegalArgumentException("Invalid tenant database configuration");
        }

        if (repository.existsByTenantId(configuration.getTenantId())) {
            throw new IllegalArgumentException("Tenant already exists: " + configuration.getTenantId());
        }

        configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.PROVISIONING);
        configuration.setProvisioningStartTime(LocalDateTime.now());
        configuration.setCreatedAt(LocalDateTime.now());

        TenantDatabaseConfiguration saved = repository.save(configuration);

        // Provision the tenant database
        provisionTenantDatabase(saved);

        return saved;
    }

    /**
     * Provision tenant database.
     */
    private void provisionTenantDatabase(TenantDatabaseConfiguration configuration) {
        log.info("Provisioning database for tenant: {}", configuration.getTenantId());

        try {
            switch (configuration.getStrategy()) {
                case DATABASE_PER_TENANT:
                    provisionDatabasePerTenant(configuration);
                    break;
                case SCHEMA_PER_TENANT:
                    provisionSchemaPerTenant(configuration);
                    break;
                case SHARED_DATABASE:
                    setupSharedDatabaseTenant(configuration);
                    break;
                case HYBRID:
                    provisionHybridTenant(configuration);
                    break;
            }

            configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.ACTIVE);
            configuration.setProvisioningCompletedAt(LocalDateTime.now());

            // Cache the data source
            cacheTenantDataSource(configuration);

            log.info("Tenant database provisioned successfully: {}", configuration.getTenantId());

        } catch (Exception e) {
            configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.ERROR);
            log.error("Failed to provision tenant database {}: {}",
                    configuration.getTenantId(), e.getMessage());
        }

        repository.save(configuration);
    }

    /**
     * Provision database per tenant.
     */
    private void provisionDatabasePerTenant(TenantDatabaseConfiguration configuration) {
        log.info("Provisioning dedicated database for tenant: {}", configuration.getTenantId());

        // TODO: Implement database provisioning
        // 1. Create new database
        // 2. Run migration scripts
        // 3. Create connection pool
        // 4. Cache data source
    }

    /**
     * Provision schema per tenant.
     */
    private void provisionSchemaPerTenant(TenantDatabaseConfiguration configuration) {
        log.info("Provisioning dedicated schema for tenant: {}", configuration.getTenantId());

        // TODO: Implement schema provisioning
        // 1. Create new schema
        // 2. Run migration scripts
        // 3. Set up schema-specific connection
    }

    /**
     * Setup tenant in shared database.
     */
    private void setupSharedDatabaseTenant(TenantDatabaseConfiguration configuration) {
        log.info("Setting up tenant in shared database: {}", configuration.getTenantId());

        // TODO: Implement shared database setup
        // 1. Ensure discriminator columns exist
        // 2. Set up tenant-specific views if needed
    }

    /**
     * Provision hybrid tenant.
     */
    private void provisionHybridTenant(TenantDatabaseConfiguration configuration) {
        log.info("Provisioning hybrid tenant: {}", configuration.getTenantId());

        // TODO: Implement hybrid provisioning
    }

    /**
     * Cache tenant data source.
     */
    private void cacheTenantDataSource(TenantDatabaseConfiguration configuration) {
        if (configuration.getConnectionPoolName() != null) {
            connectionPoolService.getDataSource(
                    configuration.getTenantId(),
                    configuration.getConnectionPoolName())
                    .ifPresent(ds -> tenantDataSources.put(configuration.getTenantId(), ds));
        }
    }

    /**
     * Get tenant configuration.
     */
    @Cacheable(value = "tenantDatabases", key = "#tenantId")
    public Optional<TenantDatabaseConfiguration> getTenant(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    /**
     * Get tenant data source.
     */
    public Optional<DataSource> getTenantDataSource(String tenantId) {
        // Check cache first
        DataSource cached = tenantDataSources.get(tenantId);
        if (cached != null) {
            return Optional.of(cached);
        }

        // Load from repository
        Optional<TenantDatabaseConfiguration> config = repository.findByTenantId(tenantId);
        if (config.isEmpty() || !config.get().getIsActive()) {
            return Optional.empty();
        }

        TenantDatabaseConfiguration tenantConfig = config.get();

        if (tenantConfig.getConnectionPoolName() != null) {
            Optional<HikariDataSource> poolDs = connectionPoolService.getDataSource(
                    tenantConfig.getTenantId(),
                    tenantConfig.getConnectionPoolName());

            poolDs.ifPresent(ds -> tenantDataSources.put(tenantId, ds));
            return poolDs.map(ds -> ds);
        }

        return Optional.empty();
    }

    /**
     * Get all tenants.
     */
    public List<TenantDatabaseConfiguration> getAllTenants() {
        return repository.findAll();
    }

    /**
     * Get tenants by status.
     */
    public List<TenantDatabaseConfiguration> getTenantsByStatus(TenantDatabaseConfiguration.TenantStatus status) {
        return repository.findAllByStatus(status);
    }

    /**
     * Get tenants by tier.
     */
    public List<TenantDatabaseConfiguration> getTenantsByTier(TenantDatabaseConfiguration.TenantTier tier) {
        return repository.findAllByTier(tier);
    }

    /**
     * Update tenant configuration.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public TenantDatabaseConfiguration updateTenant(String id, @Valid TenantDatabaseConfiguration configuration) {
        log.info("Updating tenant database configuration: {}", configuration.getTenantId());

        TenantDatabaseConfiguration existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + id));

        if (!existing.getTenantId().equals(configuration.getTenantId())) {
            throw new IllegalArgumentException("Cannot change tenant ID");
        }

        configuration.setId(id);
        configuration.setVersion(existing.getVersion() + 1);
        configuration.setCreatedAt(existing.getCreatedAt());

        // Invalidate cached data source
        tenantDataSources.remove(existing.getTenantId());

        return repository.save(configuration);
    }

    /**
     * Activate tenant.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public TenantDatabaseConfiguration activateTenant(String tenantId) {
        TenantDatabaseConfiguration configuration = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        configuration.setIsActive(true);
        configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.ACTIVE);

        TenantDatabaseConfiguration saved = repository.save(configuration);
        cacheTenantDataSource(saved);

        return saved;
    }

    /**
     * Deactivate tenant.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public TenantDatabaseConfiguration deactivateTenant(String tenantId) {
        TenantDatabaseConfiguration configuration = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        configuration.setIsActive(true); // Keep active but suspend
        configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.SUSPENDED);

        // Remove cached data source
        tenantDataSources.remove(tenantId);

        return repository.save(configuration);
    }

    /**
     * Deprovision tenant.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public void deprovisionTenant(String tenantId) {
        log.info("Deprovisioning tenant: {}", tenantId);

        TenantDatabaseConfiguration configuration = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.DEPROVISIONING);
        configuration.setIsActive(false);
        repository.save(configuration);

        try {
            // Perform deprovisioning based on strategy
            switch (configuration.getStrategy()) {
                case DATABASE_PER_TENANT:
                    deprovisionDatabasePerTenant(configuration);
                    break;
                case SCHEMA_PER_TENANT:
                    deprovisionSchemaPerTenant(configuration);
                    break;
                case SHARED_DATABASE:
                    cleanupSharedDatabaseTenant(configuration);
                    break;
                default:
                    log.warn("Unknown strategy for deprovisioning: {}", configuration.getStrategy());
            }

            configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.DEPROVISIONED);
            configuration.setDeprovisionedAt(LocalDateTime.now());

            // Remove cached data source
            tenantDataSources.remove(tenantId);

            log.info("Tenant deprovisioned successfully: {}", tenantId);

        } catch (Exception e) {
            configuration.setStatus(TenantDatabaseConfiguration.TenantStatus.ERROR);
            log.error("Failed to deprovision tenant {}: {}", tenantId, e.getMessage());
        }

        repository.save(configuration);
    }

    /**
     * Deprovision database per tenant.
     */
    private void deprovisionDatabasePerTenant(TenantDatabaseConfiguration configuration) {
        log.info("Dropping database for tenant: {}", configuration.getTenantId());
        // TODO: Implement database drop
    }

    /**
     * Deprovision schema per tenant.
     */
    private void deprovisionSchemaPerTenant(TenantDatabaseConfiguration configuration) {
        log.info("Dropping schema for tenant: {}", configuration.getTenantId());
        // TODO: Implement schema drop
    }

    /**
     * Cleanup tenant in shared database.
     */
    private void cleanupSharedDatabaseTenant(TenantDatabaseConfiguration configuration) {
        log.info("Cleaning up data for tenant: {}", configuration.getTenantId());
        // TODO: Implement data cleanup for shared database
    }

    /**
     * Check if tenant can accept new connections.
     */
    public boolean canTenantAcceptConnection(String tenantId) {
        Optional<TenantDatabaseConfiguration> config = repository.findByTenantId(tenantId);
        return config.isPresent() && config.get().canAcceptConnection();
    }

    /**
     * Get tenant statistics.
     */
    public Map<String, Object> getTenantStatistics(String tenantId) {
        Optional<TenantDatabaseConfiguration> config = repository.findByTenantId(tenantId);

        if (config.isEmpty()) {
            return Collections.emptyMap();
        }

        TenantDatabaseConfiguration tenant = config.get();
        Map<String, Object> stats = new HashMap<>();
        stats.put("tenantId", tenant.getTenantId());
        stats.put("status", tenant.getStatus());
        stats.put("strategy", tenant.getStrategy());
        stats.put("tier", tenant.getTier());
        stats.put("currentConnections", tenant.getCurrentConnections());
        stats.put("maxConnections", tenant.getEffectiveMaxConnections());
        stats.put("isActive", tenant.getIsActive());
        stats.put("createdAt", tenant.getCreatedAt());

        return stats;
    }

    /**
     * Get all tenant statistics.
     */
    public Map<String, Object> getAllTenantsStatistics() {
        List<TenantDatabaseConfiguration> allTenants = repository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTenants", allTenants.size());
        stats.put("activeTenants", repository.countByIsActiveAndStatus(true,
                TenantDatabaseConfiguration.TenantStatus.ACTIVE));

        Map<String, Long> countsByStatus = new HashMap<>();
        for (TenantDatabaseConfiguration.TenantStatus status : TenantDatabaseConfiguration.TenantStatus.values()) {
            long count = allTenants.stream()
                    .filter(t -> t.getStatus() == status)
                    .count();
            countsByStatus.put(status.name(), count);
        }
        stats.put("byStatus", countsByStatus);

        Map<String, Long> countsByTier = new HashMap<>();
        for (TenantDatabaseConfiguration.TenantTier tier : TenantDatabaseConfiguration.TenantTier.values()) {
            long count = repository.countByTier(tier);
            countsByTier.put(tier.name(), count);
        }
        stats.put("byTier", countsByTier);

        return stats;
    }

    /**
     * Upgrade tenant tier.
     */
    @Transactional
    @CacheEvict(value = "tenantDatabases", allEntries = true)
    public TenantDatabaseConfiguration upgradeTier(String tenantId, TenantDatabaseConfiguration.TenantTier newTier) {
        TenantDatabaseConfiguration configuration = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        configuration.setTier(newTier);

        return repository.save(configuration);
    }
}
