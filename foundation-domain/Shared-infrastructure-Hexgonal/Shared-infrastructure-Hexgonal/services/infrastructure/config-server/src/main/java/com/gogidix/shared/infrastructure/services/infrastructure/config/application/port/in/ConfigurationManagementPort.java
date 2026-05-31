package com.gogidix.shared.infrastructure.services.infrastructure.config.application.port.in;

import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.model.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Input port for configuration management operations.
 * Defines the contract for configuration management use cases.
 */
public interface ConfigurationManagementPort {

    /**
     * Register a new configuration.
     */
    Configuration registerConfiguration(String applicationName, String profile, String label,
                                       Map<String, Object> properties, String createdBy);

    /**
     * Update an existing configuration.
     */
    Optional<Configuration> updateConfiguration(String configId, Map<String, Object> newProperties,
                                               String updatedBy);

    /**
     * Delete a configuration.
     */
    boolean deleteConfiguration(String configId);

    /**
     * Get configuration by application, profile, and label.
     */
    Optional<Configuration> getConfiguration(String applicationName, String profile, String label);

    /**
     * Register tenant-specific configuration.
     */
    Configuration registerTenantConfiguration(String tenantId, String applicationName,
                                             Map<String, Object> properties, String createdBy);

    /**
     * Get tenant-specific configuration.
     */
    Optional<Configuration> getTenantConfiguration(String tenantId, String applicationName);

    /**
     * Get all configurations.
     */
    List<Configuration> getAllConfigurations();

    /**
     * Get configurations by application name.
     */
    List<Configuration> getConfigurationsByApplication(String applicationName);

    /**
     * Reload all configurations.
     */
    void reloadAllConfigurations();

    /**
     * Reload configuration for a specific application.
     */
    void reloadApplicationConfiguration(String applicationName);
}
