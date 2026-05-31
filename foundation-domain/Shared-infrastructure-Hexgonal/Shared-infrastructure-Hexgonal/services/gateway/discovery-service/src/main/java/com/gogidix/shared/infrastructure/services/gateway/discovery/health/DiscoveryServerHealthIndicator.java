package com.gogidix.shared.infrastructure.services.gateway.discovery.health;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistryQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.netflix.eureka.server.EurekaDashboardProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Health indicator for Eureka Discovery Server itself.
 * Monitors:
 * - Server status
 * - Dashboard availability
 * - Self-preservation mode
 */
@Component
public class DiscoveryServerHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private ServiceRegistryQueryPort queryPort;

    @Autowired(required = false)
    private EurekaDashboardProperties dashboardProperties;

    @Value("${eureka.server.enable-self-preservation:true}")
    private boolean selfPreservationEnabled;

    @Value("${info.app.name:discovery-service}")
    private String appName;

    @Value("${info.app.version:1.0.0}")
    private String appVersion;

    @Override
    public Health health() {
        Health.Builder builder = Health.up();

        try {
            // Application info
            builder.withDetail("appName", appName)
                   .withDetail("appVersion", appVersion)
                   .withDetail("status", "UP");

            // Registry info from our domain
            if (queryPort != null) {
                ServiceRegistryStatistics stats = queryPort.getStatistics();
                builder.withDetail("registryInitialized", stats.getTotalApplications() >= 0)
                       .withDetail("numberOfRegisteredApplications", stats.getTotalApplications())
                       .withDetail("totalInstances", stats.getTotalInstances())
                       .withDetail("isSelfPreservationModeEnabled", queryPort.isSelfPreservationEnabled())
                       .withDetail("renewalThresholdPercent", queryPort.getRenewalThreshold());
            }

            // Dashboard status
            builder.withDetail("dashboardEnabled", dashboardProperties != null && dashboardProperties.isEnabled())
                   .withDetail("dashboardPath", dashboardProperties != null ? dashboardProperties.getPath() : "/eureka");

            // Health check time
            builder.withDetail("healthCheckTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            // Operating mode
            builder.withDetail("operatingMode", getOperatingMode());

        } catch (Exception e) {
            builder = Health.down()
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage())
                    .withDetail("healthCheckTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        return builder.build();
    }

    /**
     * Determine the operating mode based on configuration.
     */
    private String getOperatingMode() {
        return "STANDALONE";
    }
}
