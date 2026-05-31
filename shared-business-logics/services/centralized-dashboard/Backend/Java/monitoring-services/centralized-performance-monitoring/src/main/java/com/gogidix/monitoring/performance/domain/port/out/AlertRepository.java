package com.gogidix.monitoring.performance.domain.port.out;

import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port (repository) for alert persistence.
 */
public interface AlertRepository {

    /**
     * Save an alert.
     */
    PerformanceAlert save(PerformanceAlert alert);

    /**
     * Find alert by ID.
     */
    Optional<PerformanceAlert> findById(String id);

    /**
     * Find active alerts for a tenant and service.
     */
    List<PerformanceAlert> findActiveAlerts(String tenantId, String serviceId);

    /**
     * Update alert status.
     */
    PerformanceAlert updateStatus(String id, String status);
}
