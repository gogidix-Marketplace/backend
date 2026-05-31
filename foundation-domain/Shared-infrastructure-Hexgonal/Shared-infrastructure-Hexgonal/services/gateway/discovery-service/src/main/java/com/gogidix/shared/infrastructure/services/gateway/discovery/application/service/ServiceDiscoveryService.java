package com.gogidix.shared.infrastructure.services.gateway.discovery.application.service;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceDiscoveryPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Application service implementing service discovery operations.
 * Acts as the use case orchestrator for service discovery.
 */
@Service
public class ServiceDiscoveryService implements ServiceDiscoveryPort {

    private static final Logger log = LoggerFactory.getLogger(ServiceDiscoveryService.class);

    private final ServiceRegistry serviceRegistry;

    public ServiceDiscoveryService(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public Optional<ServiceInstance> getInstance(String instanceId) {
        log.debug("Looking up instance: {}", instanceId);

        return serviceRegistry.getInstance(instanceId);
    }

    @Override
    public List<ServiceInstance> getInstancesByApp(String appName) {
        log.debug("Looking up instances for app: {}", appName);

        List<ServiceInstance> instances = serviceRegistry.getInstancesByApp(appName);

        log.debug("Found {} instances for app: {}", instances.size(), appName);

        return instances;
    }

    @Override
    public List<ServiceInstance> getUpInstancesByApp(String appName) {
        log.debug("Looking up UP instances for app: {}", appName);

        List<ServiceInstance> instances = serviceRegistry.getUpInstancesByApp(appName);

        log.debug("Found {} UP instances for app: {}", instances.size(), appName);

        return instances;
    }

    @Override
    public Collection<String> getAllApplications() {
        Collection<String> applications = serviceRegistry.getAllApplications();

        log.debug("Found {} registered applications", applications.size());

        return applications;
    }

    @Override
    public Collection<ServiceInstance> getAllInstances() {
        Collection<ServiceInstance> instances = serviceRegistry.getAllInstances();

        log.debug("Found {} total instances", instances.size());

        return instances;
    }

    @Override
    public List<ServiceInstance> getInstancesByZone(String zone) {
        log.debug("Looking up instances in zone: {}", zone);

        return serviceRegistry.getInstancesByZone(zone);
    }

    @Override
    public List<ServiceInstance> getInstancesByDataCenter(String dataCenter) {
        log.debug("Looking up instances in data center: {}", dataCenter);

        return serviceRegistry.getInstancesByDataCenter(dataCenter);
    }
}
