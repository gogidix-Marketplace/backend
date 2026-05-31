package com.gogidix.monitoring.servicehealthservice.domain.port.out;

/**
 * Output port for publishing health check events.
 */
public interface HealthCheckEventPublisherPort {

    /**
     * Publish a health status changed event.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param oldStatus  the old status
     * @param newStatus  the new status
     * @param healthScore the health score
     */
    void publishHealthStatusChanged(
            String tenantId,
            String serviceName,
            String oldStatus,
            String newStatus,
            Integer healthScore
    );

    /**
     * Publish a service down event.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param reason     the reason
     */
    void publishServiceDown(
            String tenantId,
            String serviceName,
            String reason
    );

    /**
     * Publish a service recovered event.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param downtimeSeconds the downtime duration
     */
    void publishServiceRecovered(
            String tenantId,
            String serviceName,
            Long downtimeSeconds
    );
}
