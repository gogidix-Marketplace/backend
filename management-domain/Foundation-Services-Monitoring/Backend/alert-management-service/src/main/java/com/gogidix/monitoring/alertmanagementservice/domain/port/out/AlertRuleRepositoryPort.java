package com.gogidix.monitoring.alertmanagementservice.domain.port.out;

import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;

import java.util.List;
import java.util.Optional;

/**
 * Output port for alert rule repository operations.
 */
public interface AlertRuleRepositoryPort {

    /**
     * Save an alert rule.
     */
    AlertRule save(AlertRule rule);

    /**
     * Find by ID.
     */
    Optional<AlertRule> findById(String id);

    /**
     * Find all rules for a tenant.
     */
    List<AlertRule> findByTenantId(String tenantId);

    /**
     * Find enabled rules for a tenant.
     */
    List<AlertRule> findEnabledByTenantId(String tenantId);

    /**
     * Find rules by service.
     */
    List<AlertRule> findByTenantAndService(String tenantId, String serviceName);

    /**
     * Delete by ID.
     */
    void deleteById(String id);

    /**
     * Check if rule exists.
     */
    boolean existsById(String id);
}
