package com.gogidix.centralconfiguration.configserver.domain.repository;

import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;

import java.util.List;

/**
 * Repository interface for Configuration History.
 * Defines the contract for configuration audit trail operations.
 */
public interface ConfigurationHistoryRepository {

    ConfigurationHistory save(ConfigurationHistory history);

    List<ConfigurationHistory> findByConfigurationId(Long configurationId);

    List<ConfigurationHistory> findByTenantIdAndApplicationName(
            String tenantId, String applicationName, int page, int size);

    List<ConfigurationHistory> findByTenantId(String tenantId, int page, int size);
}
