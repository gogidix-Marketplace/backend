package com.gogidix.shared.infrastructure.services.gateway.discovery.infrastructure.adapter;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceDiscoveryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistryQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Infrastructure adapter that bridges the hexagonal domain with Eureka server.
 * This adapter provides a clean interface between our domain model and external integrations.
 */
@Component
public class EurekaServerAdapter {

    private static final Logger log = LoggerFactory.getLogger(EurekaServerAdapter.class);

    private final ServiceDiscoveryPort discoveryPort;
    private final ServiceRegistryQueryPort queryPort;

    public EurekaServerAdapter(ServiceDiscoveryPort discoveryPort,
                               ServiceRegistryQueryPort queryPort) {
        this.discoveryPort = discoveryPort;
        this.queryPort = queryPort;
        log.info("EurekaServerAdapter initialized");
    }

    /**
     * Get all applications from the registry.
     */
    public Collection<String> getApplications() {
        return discoveryPort.getAllApplications();
    }

    /**
     * Get all instances for an application.
     */
    public List<ServiceInstance> getInstancesByApp(String appName) {
        return discoveryPort.getInstancesByApp(appName);
    }

    /**
     * Get all UP instances for an application.
     */
    public List<ServiceInstance> getUpInstancesByApp(String appName) {
        return discoveryPort.getUpInstancesByApp(appName);
    }

    /**
     * Get a specific instance by ID.
     */
    public ServiceInstance getInstance(String instanceId) {
        return discoveryPort.getInstance(instanceId).orElse(null);
    }

    /**
     * Get registry statistics from the domain layer.
     */
    public ServiceRegistryStatistics getRegistryStats() {
        return queryPort.getStatistics();
    }

    /**
     * Get the number of registered applications.
     */
    public int getApplicationCount() {
        return queryPort.getApplicationCount();
    }

    /**
     * Get the number of registered instances.
     */
    public int getInstanceCount() {
        return queryPort.getInstanceCount();
    }

    /**
     * Check if self-preservation is enabled.
     */
    public boolean isSelfPreservationEnabled() {
        return queryPort.isSelfPreservationEnabled();
    }
}
