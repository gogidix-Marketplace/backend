package com.gogidix.monitoring.alertmanagementservice.domain.port.out;

import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertHistory;

import java.util.List;

/**
 * Output port for alert history repository operations.
 */
public interface AlertHistoryRepositoryPort {

    /**
     * Save an alert history entry.
     */
    AlertHistory save(AlertHistory history);

    /**
     * Find history by alert ID.
     */
    List<AlertHistory> findByAlertId(String alertId);

    /**
     * Find history by tenant.
     */
    List<AlertHistory> findByTenantId(String tenantId);

    /**
     * Delete history older than timestamp.
     */
    long deleteOlderThan(java.time.Instant timestamp);
}
