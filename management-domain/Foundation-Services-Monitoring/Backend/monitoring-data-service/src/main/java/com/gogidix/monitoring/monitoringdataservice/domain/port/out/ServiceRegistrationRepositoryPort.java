package com.gogidix.monitoring.monitoringdataservice.domain.port.out;

import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;

import java.util.List;
import java.util.Optional;

/**
 * Output port for service registration repository operations.
 */
public interface ServiceRegistrationRepositoryPort {

    /**
     * Save a service registration.
     *
     * @param registration the registration to save
     * @return the saved registration
     */
    ServiceRegistration save(ServiceRegistration registration);

    /**
     * Find by service ID.
     *
     * @param serviceId the service ID
     * @return the registration if found
     */
    Optional<ServiceRegistration> findById(String serviceId);

    /**
     * Find by tenant and service name.
     *
     * @param tenantId    the tenant ID
     * @param serviceName the service name
     * @return the registration if found
     */
    Optional<ServiceRegistration> findByTenantAndServiceName(String tenantId, String serviceName);

    /**
     * Find all services for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of registrations
     */
    List<ServiceRegistration> findByTenantId(String tenantId);

    /**
     * Find all active services for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active registrations
     */
    List<ServiceRegistration> findActiveByTenantId(String tenantId);

    /**
     * Find services by type.
     *
     * @param tenantId    the tenant ID
     * @param serviceType the service type
     * @return list of registrations
     */
    List<ServiceRegistration> findByTenantIdAndServiceType(
            String tenantId,
            com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType serviceType
    );

    /**
     * Delete a service registration.
     *
     * @param serviceId the service ID
     */
    void deleteById(String serviceId);

    /**
     * Check if a service exists.
     *
     * @param tenantId    the tenant ID
     * @param serviceName the service name
     * @return true if exists
     */
    boolean existsByTenantAndServiceName(String tenantId, String serviceName);

    /**
     * Update the heartbeat timestamp for a service.
     *
     * @param serviceId the service ID
     */
    void updateHeartbeat(String serviceId);
}
