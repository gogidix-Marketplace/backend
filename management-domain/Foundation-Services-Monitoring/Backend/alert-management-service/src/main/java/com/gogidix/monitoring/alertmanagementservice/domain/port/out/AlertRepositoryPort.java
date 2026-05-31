package com.gogidix.monitoring.alertmanagementservice.domain.port.out;

import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Output port for alert repository operations.
 */
public interface AlertRepositoryPort {

    /**
     * Save an alert.
     */
    Alert save(Alert alert);

    /**
     * Find by ID.
     */
    Optional<Alert> findById(String id);

    /**
     * Find all alerts for a tenant.
     */
    List<Alert> findByTenantId(String tenantId);

    /**
     * Find alerts by tenant and status.
     */
    List<Alert> findByTenantIdAndStatus(String tenantId, Alert.AlertStatus status);

    /**
     * Find alerts by tenant and severity.
     */
    List<Alert> findByTenantIdAndSeverity(String tenantId, AlertRule.AlertSeverity severity);

    /**
     * Find open alerts for a service.
     */
    List<Alert> findOpenAlertsByService(String tenantId, String serviceName);

    /**
     * Delete by ID.
     */
    void deleteById(String id);

    /**
     * Delete alerts older than timestamp.
     */
    long deleteOlderThan(Instant timestamp);
}
