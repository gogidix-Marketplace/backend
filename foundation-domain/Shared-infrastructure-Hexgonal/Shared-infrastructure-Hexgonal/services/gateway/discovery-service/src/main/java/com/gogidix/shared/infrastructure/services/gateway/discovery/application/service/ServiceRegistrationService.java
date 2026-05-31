package com.gogidix.shared.infrastructure.services.gateway.discovery.application.service;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistrationPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service implementing service registration operations.
 * Acts as the use case orchestrator for service registration.
 */
@Service
public class ServiceRegistrationService implements ServiceRegistrationPort {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistrationService.class);

    private final ServiceRegistry serviceRegistry;

    public ServiceRegistrationService(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public ServiceInstance register(ServiceRegistration registration, String instanceId) {
        log.info("Registering service: appName={}, instanceId={}, host={}, port={}",
                registration.getAppName(), instanceId, registration.getHostName(), registration.getPort());

        ServiceInstance instance = serviceRegistry.register(registration, instanceId);

        log.debug("Service registered successfully: {}", instance);

        return instance;
    }

    @Override
    public Optional<ServiceInstance> deregister(String instanceId) {
        log.info("Deregistering service instance: {}", instanceId);

        Optional<ServiceInstance> instance = serviceRegistry.deregister(instanceId);

        instance.ifPresentOrElse(
                i -> log.debug("Service deregistered successfully: {}", instanceId),
                () -> log.warn("Service instance not found for deregistration: {}", instanceId)
        );

        return instance;
    }

    @Override
    public Optional<ServiceInstance> renew(String instanceId) {
        log.debug("Renewing lease for instance: {}", instanceId);

        Optional<ServiceInstance> instance = serviceRegistry.renew(instanceId);

        if (instance.isEmpty()) {
            log.warn("Failed to renew lease for non-existent instance: {}", instanceId);
        }

        return instance;
    }

    @Override
    public Optional<ServiceInstance> updateStatus(String instanceId, String status) {
        log.info("Updating status for instance: {} to {}", instanceId, status);

        Optional<ServiceInstance> instance = serviceRegistry.updateStatus(instanceId, status);

        instance.ifPresentOrElse(
                i -> log.debug("Status updated successfully: {} -> {}", instanceId, status),
                () -> log.warn("Service instance not found for status update: {}", instanceId)
        );

        return instance;
    }
}
