package com.gogidix.monitoring.servicehealthservice.domain.port.out;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Output port for service uptime repository operations.
 */
public interface ServiceUptimeRepositoryPort {

    /**
     * Save a service uptime record.
     */
    ServiceUptime save(ServiceUptime uptime);

    /**
     * Find ongoing incidents for a service.
     */
    List<ServiceUptime> findOngoingByTenantAndService(String tenantId, String serviceName);

    /**
     * Find uptime history for a service.
     */
    List<ServiceUptime> findByTenantAndServiceAndTimeRange(
            String tenantId,
            String serviceName,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find all incidents for a tenant.
     */
    List<ServiceUptime> findByTenantId(String tenantId);

    /**
     * Delete records older than timestamp.
     */
    long deleteOlderThan(Instant timestamp);

    /**
     * Calculate uptime percentage for a service.
     */
    Double calculateUptimePercentage(String tenantId, String serviceName, Instant since);
}
