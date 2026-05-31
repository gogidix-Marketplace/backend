package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository;

import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.ServiceRegistrationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for service registrations.
 */
@Repository
public interface SpringDataServiceRegistrationRepository extends MongoRepository<ServiceRegistrationDocument, String> {

    /**
     * Find by service ID.
     */
    Optional<ServiceRegistrationDocument> findByServiceId(String serviceId);

    /**
     * Find by tenant and service name.
     */
    Optional<ServiceRegistrationDocument> findByTenantIdAndServiceName(String tenantId, String serviceName);

    /**
     * Find all by tenant ID.
     */
    List<ServiceRegistrationDocument> findByTenantId(String tenantId);

    /**
     * Find active services by tenant ID.
     */
    List<ServiceRegistrationDocument> findByTenantIdAndStatusAndEnabledTrue(
            String tenantId,
            String status
    );

    /**
     * Find by tenant and service type.
     */
    List<ServiceRegistrationDocument> findByTenantIdAndServiceType(
            String tenantId,
            String serviceType
    );

    /**
     * Check if exists by tenant and service name.
     */
    boolean existsByTenantIdAndServiceName(String tenantId, String serviceName);

    /**
     * Delete by service ID.
     */
    void deleteByServiceId(String serviceId);
}
