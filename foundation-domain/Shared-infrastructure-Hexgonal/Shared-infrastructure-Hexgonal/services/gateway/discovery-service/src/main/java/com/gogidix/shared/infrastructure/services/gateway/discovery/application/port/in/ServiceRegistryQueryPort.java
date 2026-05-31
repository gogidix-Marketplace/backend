package com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;

/**
 * Input port for service registry queries and management operations.
 * Provides statistics and administrative functions for the registry.
 */
public interface ServiceRegistryQueryPort {

    /**
     * Get current registry statistics.
     *
     * @return the registry statistics
     */
    ServiceRegistryStatistics getStatistics();

    /**
     * Get the total number of registered applications.
     *
     * @return the number of applications
     */
    int getApplicationCount();

    /**
     * Get the total number of registered instances.
     *
     * @return the number of instances
     */
    int getInstanceCount();

    /**
     * Evict expired instances from the registry.
     *
     * @return the number of evicted instances
     */
    int evictExpired();

    /**
     * Check if the registry is in self-preservation mode.
     *
     * @return true if self-preservation is enabled
     */
    boolean isSelfPreservationEnabled();

    /**
     * Get the renewal threshold percentage.
     *
     * @return the threshold (0-100)
     */
    int getRenewalThreshold();
}
