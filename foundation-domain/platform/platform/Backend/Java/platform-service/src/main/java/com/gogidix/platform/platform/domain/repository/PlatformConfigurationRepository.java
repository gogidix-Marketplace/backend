package com.gogidix.platform.platform.domain.repository;

import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for PlatformConfiguration entity.
 */
@Repository
public interface PlatformConfigurationRepository extends JpaRepository<PlatformConfiguration, String> {

    /**
     * Find configuration by key
     */
    Optional<PlatformConfiguration> findByConfigKey(String configKey);

    /**
     * Find configurations by tenant ID
     */
    List<PlatformConfiguration> findByTenantId(String tenantId);

    /**
     * Find configurations by tenant and status
     */
    List<PlatformConfiguration> findByTenantIdAndStatus(String tenantId, PlatformConfiguration.ConfigStatus status);

    /**
     * Find configurations by environment
     */
    List<PlatformConfiguration> findByEnvironment(PlatformConfiguration.Environment environment);

    /**
     * Find effective configurations for tenant
     */
    @Query("SELECT c FROM PlatformConfiguration c WHERE c.tenantId = :tenantId " +
           "AND c.status = 'ACTIVE' AND (" +
           "c.effectiveFrom IS NULL OR c.effectiveFrom <= CURRENT_TIMESTAMP) AND (" +
           "c.effectiveUntil IS NULL OR c.effectiveUntil > CURRENT_TIMESTAMP)")
    List<PlatformConfiguration> findEffectiveConfigurations(@Param("tenantId") String tenantId);

    /**
     * Find configuration by tenant and key
     */
    Optional<PlatformConfiguration> findByTenantIdAndConfigKey(String tenantId, String configKey);

    /**
     * Check if key exists
     */
    boolean existsByConfigKey(String configKey);
}
