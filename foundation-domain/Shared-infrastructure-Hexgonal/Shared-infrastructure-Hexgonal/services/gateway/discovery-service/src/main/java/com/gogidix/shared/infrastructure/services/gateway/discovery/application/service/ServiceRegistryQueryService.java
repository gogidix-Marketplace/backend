package com.gogidix.shared.infrastructure.services.gateway.discovery.application.service;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistryQueryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Application service for registry queries and management.
 * Provides statistics and administrative functions.
 */
@Service
public class ServiceRegistryQueryService implements ServiceRegistryQueryPort {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistryQueryService.class);

    private final ServiceRegistry serviceRegistry;

    @Value("${eureka.server.renewal-percent-threshold:0.85}")
    private double renewalThreshold;

    @Value("${eureka.server.enable-self-preservation:true}")
    private boolean selfPreservationEnabled;

    public ServiceRegistryQueryService(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public ServiceRegistryStatistics getStatistics() {
        ServiceRegistryStatistics stats = serviceRegistry.getStatistics();

        log.debug("Registry statistics: {}", stats);

        return stats;
    }

    @Override
    public int getApplicationCount() {
        int count = serviceRegistry.size();

        log.debug("Application count: {}", count);

        return count;
    }

    @Override
    public int getInstanceCount() {
        int count = serviceRegistry.size();

        log.debug("Instance count: {}", count);

        return count;
    }

    @Override
    public int evictExpired() {
        log.info("Evicting expired instances from registry");

        var expired = serviceRegistry.evictExpired();

        log.info("Evicted {} expired instances", expired.size());

        return expired.size();
    }

    @Override
    public boolean isSelfPreservationEnabled() {
        return selfPreservationEnabled;
    }

    @Override
    public int getRenewalThreshold() {
        return (int) (renewalThreshold * 100);
    }
}
