package com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceRegistration;

import java.util.Optional;

/**
 * Input port for service registration operations.
 * This is the primary interface for services to register with the discovery server.
 */
public interface ServiceRegistrationPort {

    /**
     * Register a new service instance.
     *
     * @param registration the registration details
     * @param instanceId   the unique instance ID
     * @return the registered service instance
     */
    ServiceInstance register(ServiceRegistration registration, String instanceId);

    /**
     * Deregister a service instance.
     *
     * @param instanceId the instance ID to deregister
     * @return the deregistered instance, if found
     */
    Optional<ServiceInstance> deregister(String instanceId);

    /**
     * Renew the lease for a service instance.
     *
     * @param instanceId the instance ID to renew
     * @return the renewed instance, if found
     */
    Optional<ServiceInstance> renew(String instanceId);

    /**
     * Update the status of a service instance.
     *
     * @param instanceId the instance ID to update
     * @param status     the new status (UP, DOWN, OUT_OF_SERVICE, etc.)
     * @return the updated instance, if found
     */
    Optional<ServiceInstance> updateStatus(String instanceId, String status);
}
