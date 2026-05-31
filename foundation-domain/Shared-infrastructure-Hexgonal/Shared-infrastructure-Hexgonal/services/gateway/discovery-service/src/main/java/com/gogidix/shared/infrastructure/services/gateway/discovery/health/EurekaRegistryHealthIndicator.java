package com.gogidix.shared.infrastructure.services.gateway.discovery.health;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceDiscoveryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistryQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Health indicator for Eureka Server registry.
 * Monitors:
 * - Registry size (number of registered applications)
 * - Instance counts per application
 */
@Component
public class EurekaRegistryHealthIndicator implements HealthIndicator {

    private static final int REGISTRY_SIZE_WARNING_THRESHOLD = 50;
    private static final int REGISTRY_SIZE_CRITICAL_THRESHOLD = 100;

    @Autowired(required = false)
    private ServiceDiscoveryPort discoveryPort;

    @Autowired(required = false)
    private ServiceRegistryQueryPort queryPort;

    @Override
    public Health health() {
        Health.Builder builder = Health.up();

        try {
            if (queryPort != null) {
                ServiceRegistryStatistics stats = queryPort.getStatistics();

                builder.withDetail("registrySize", stats.getTotalInstances())
                       .withDetail("registeredApplications", stats.getTotalApplications())
                       .withDetail("upInstances", stats.getUpInstances())
                       .withDetail("downInstances", stats.getDownInstances())
                       .withDetail("instanceCounts", stats.getInstancesPerApp())
                       .withDetail("instancesPerZone", stats.getInstancesPerZone());

                // Determine health based on registry size
                if (stats.getTotalApplications() == 0) {
                    builder = Health.down()
                            .withDetail("reason", "Registry is empty - no services registered")
                            .withDetail("suggestion", "Waiting for services to register");
                } else if (stats.getTotalInstances() > REGISTRY_SIZE_CRITICAL_THRESHOLD) {
                    builder.withDetail("status", "WARNING")
                           .withDetail("reason", "Registry size exceeds critical threshold");
                } else if (stats.getTotalInstances() > REGISTRY_SIZE_WARNING_THRESHOLD) {
                    builder.withDetail("status", "WARNING")
                           .withDetail("reason", "Registry size exceeds warning threshold");
                }

                // Self-preservation status
                builder.withDetail("selfPreservationEnabled", queryPort.isSelfPreservationEnabled())
                       .withDetail("renewalThreshold", queryPort.getRenewalThreshold());
            }

        } catch (Exception e) {
            builder = Health.down()
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage());
        }

        return builder.build();
    }
}
