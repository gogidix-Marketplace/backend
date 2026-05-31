package com.gogidix.shared.infrastructure.services.infrastructure.config.application.service;

import com.gogidix.shared.infrastructure.services.infrastructure.config.application.port.in.ConfigurationManagementPort;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.aggregate.ConfigurationRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.config.domain.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Application service for configuration management.
 * Implements the hexagonal architecture pattern.
 */
@Service
public class ConfigurationManagementService implements ConfigurationManagementPort {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationManagementService.class);

    private final ConfigurationRegistry configurationRegistry;

    public ConfigurationManagementService(ConfigurationRegistry configurationRegistry) {
        this.configurationRegistry = configurationRegistry;
    }

    @Override
    public Configuration registerConfiguration(String applicationName, String profile, String label,
                                              Map<String, Object> properties, String createdBy) {
        log.info("Registering configuration: application={}, profile={}, label={}",
            applicationName, profile, label);
        return configurationRegistry.registerConfiguration(applicationName, profile, label, properties, createdBy);
    }

    @Override
    public Optional<Configuration> updateConfiguration(String configId, Map<String, Object> newProperties,
                                                      String updatedBy) {
        log.info("Updating configuration: configId={}", configId);
        return configurationRegistry.updateConfiguration(configId, newProperties, updatedBy);
    }

    @Override
    public boolean deleteConfiguration(String configId) {
        log.info("Deleting configuration: configId={}", configId);
        return configurationRegistry.deleteConfiguration(configId);
    }

    @Override
    public Optional<Configuration> getConfiguration(String applicationName, String profile, String label) {
        return configurationRegistry.getConfiguration(applicationName, profile, label);
    }

    @Override
    public Configuration registerTenantConfiguration(String tenantId, String applicationName,
                                                    Map<String, Object> properties, String createdBy) {
        log.info("Registering tenant configuration: tenantId={}, application={}", tenantId, applicationName);
        return configurationRegistry.registerTenantConfiguration(tenantId, applicationName, properties, createdBy);
    }

    @Override
    public Optional<Configuration> getTenantConfiguration(String tenantId, String applicationName) {
        return configurationRegistry.getTenantConfiguration(tenantId, applicationName);
    }

    @Override
    public List<Configuration> getAllConfigurations() {
        return configurationRegistry.getAllConfigurations();
    }

    @Override
    public List<Configuration> getConfigurationsByApplication(String applicationName) {
        return configurationRegistry.getConfigurationsByApplication(applicationName);
    }

    @Override
    public void reloadAllConfigurations() {
        log.info("Triggering reload of all configurations");
        configurationRegistry.reloadAllConfigurations();
    }

    @Override
    public void reloadApplicationConfiguration(String applicationName) {
        log.info("Triggering reload of configuration for application: {}", applicationName);
        configurationRegistry.reloadApplicationConfiguration(applicationName);
    }
}
