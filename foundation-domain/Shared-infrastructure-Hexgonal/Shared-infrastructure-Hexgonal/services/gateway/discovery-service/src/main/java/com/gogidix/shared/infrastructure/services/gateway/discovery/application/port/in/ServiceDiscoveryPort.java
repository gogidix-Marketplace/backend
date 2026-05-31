package com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Input port for service discovery operations.
 * This is the primary interface for services to discover other registered services.
 */
public interface ServiceDiscoveryPort {

    /**
     * Get a specific service instance by ID.
     *
     * @param instanceId the instance ID
     * @return the service instance, if found
     */
    Optional<ServiceInstance> getInstance(String instanceId);

    /**
     * Get all instances for a specific application.
     *
     * @param appName the application name
     * @return list of service instances
     */
    List<ServiceInstance> getInstancesByApp(String appName);

    /**
     * Get all UP instances for a specific application.
     *
     * @param appName the application name
     * @return list of UP service instances
     */
    List<ServiceInstance> getUpInstancesByApp(String appName);

    /**
     * Get all registered application names.
     *
     * @return collection of application names
     */
    Collection<String> getAllApplications();

    /**
     * Get all service instances.
     *
     * @return collection of all service instances
     */
    Collection<ServiceInstance> getAllInstances();

    /**
     * Get instances by zone.
     *
     * @param zone the zone name
     * @return list of service instances in the zone
     */
    List<ServiceInstance> getInstancesByZone(String zone);

    /**
     * Get instances by data center.
     *
     * @param dataCenter the data center name
     * @return list of service instances in the data center
     */
    List<ServiceInstance> getInstancesByDataCenter(String dataCenter);
}
