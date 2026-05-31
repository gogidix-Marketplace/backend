package com.gogidix.shared.infrastructure.services.gateway.discovery.config;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceDeregisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRegisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRenewedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the domain layer.
 * Creates the ServiceRegistry aggregate root and wires up event handlers.
 */
@Configuration
public class DomainConfig {

    private static final Logger log = LoggerFactory.getLogger(DomainConfig.class);

    @Bean
    public ServiceRegistry serviceRegistry() {
        ServiceRegistry registry = new ServiceRegistry();

        // Register event handlers for logging
        registry.onServiceRegistered(this::handleServiceRegistered);
        registry.onServiceDeregistered(this::handleServiceDeregistered);
        registry.onServiceRenewed(this::handleServiceRenewed);

        log.info("ServiceRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleServiceRegistered(ServiceRegisteredEvent event) {
        log.info("Service registered: appName={}, instanceId={}, host={}, port={}, zone={}",
                event.getAppName(), event.getInstanceId(), event.getHostName(), event.getPort(), event.getZone());
    }

    private void handleServiceDeregistered(ServiceDeregisteredEvent event) {
        log.info("Service deregistered: appName={}, instanceId={}, reason={}",
                event.getAppName(), event.getInstanceId(), event.getReason());
    }

    private void handleServiceRenewed(ServiceRenewedEvent event) {
        log.debug("Service renewed: appName={}, instanceId={}",
                event.getAppName(), event.getInstanceId());
    }
}
