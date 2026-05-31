package com.gogidix.centralconfiguration.configserver.domain.repository;

import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Configuration aggregate.
 * Defines the contract for configuration persistence operations.
 */
public interface ConfigurationRepository {

    Configuration save(Configuration configuration);

    Optional<Configuration> findById(Long id);

    Optional<Configuration> findByTenantIdAndApplicationNameAndProfileAndConfigKey(
            String tenantId, String applicationName, String profile, String configKey);

    List<Configuration> findByTenantIdAndApplicationNameAndProfile(
            String tenantId, String applicationName, String profile);

    List<Configuration> findByTenantId(String tenantId);

    List<Configuration> searchByTenantIdAndApplicationName(
            String tenantId, String applicationName, String profile, Boolean isActive);

    void delete(Configuration configuration);

    boolean existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
            String tenantId, String applicationName, String profile, String configKey);

    List<Configuration> findAll(int page, int size);
}
