package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.adapter;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.ServiceRegistrationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.ServiceRegistrationDocument;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository.SpringDataServiceRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB adapter for service registration repository.
 */
@Repository
public class ServiceRegistrationRepositoryAdapter implements ServiceRegistrationRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistrationRepositoryAdapter.class);

    private final SpringDataServiceRegistrationRepository springRepository;

    public ServiceRegistrationRepositoryAdapter(SpringDataServiceRegistrationRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public ServiceRegistration save(ServiceRegistration registration) {
        log.debug("Saving service registration: {}", registration.getServiceName());

        ServiceRegistrationDocument document = toDocument(registration);
        ServiceRegistrationDocument saved = springRepository.save(document);

        return toDomain(saved);
    }

    @Override
    public Optional<ServiceRegistration> findById(String serviceId) {
        return springRepository.findByServiceId(serviceId)
                .map(this::toDomain);
    }

    @Override
    public Optional<ServiceRegistration> findByTenantAndServiceName(String tenantId, String serviceName) {
        return springRepository.findByTenantIdAndServiceName(tenantId, serviceName)
                .map(this::toDomain);
    }

    @Override
    public List<ServiceRegistration> findByTenantId(String tenantId) {
        return springRepository.findByTenantId(tenantId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ServiceRegistration> findActiveByTenantId(String tenantId) {
        return springRepository.findByTenantIdAndStatusAndEnabledTrue(
                tenantId, ServiceRegistration.ServiceStatus.ACTIVE.name()
        ).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ServiceRegistration> findByTenantIdAndServiceType(String tenantId, MetricDataPoint.ServiceType serviceType) {
        return springRepository.findByTenantIdAndServiceType(tenantId, serviceType.name()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String serviceId) {
        log.info("Deleting service registration: {}", serviceId);
        springRepository.deleteByServiceId(serviceId);
    }

    @Override
    public boolean existsByTenantAndServiceName(String tenantId, String serviceName) {
        return springRepository.existsByTenantIdAndServiceName(tenantId, serviceName);
    }

    @Override
    public void updateHeartbeat(String serviceId) {
        springRepository.findByServiceId(serviceId).ifPresent(document -> {
            document.setLastHeartbeat(java.time.Instant.now());
            if (ServiceRegistration.ServiceStatus.REGISTERED.name().equals(document.getStatus())
                    || ServiceRegistration.ServiceStatus.INACTIVE.name().equals(document.getStatus())) {
                document.setStatus(ServiceRegistration.ServiceStatus.ACTIVE.name());
            }
            springRepository.save(document);
        });
    }

    private ServiceRegistrationDocument toDocument(ServiceRegistration domain) {
        return ServiceRegistrationDocument.builder()
                .id(domain.getServiceId())
                .serviceId(domain.getServiceId())
                .tenantId(domain.getTenantId())
                .serviceName(domain.getServiceName())
                .serviceType(domain.getServiceType() != null ? domain.getServiceType().name() : null)
                .category(domain.getCategory())
                .version(domain.getVersion())
                .description(domain.getDescription())
                .baseUrl(domain.getBaseUrl())
                .healthEndpoint(domain.getHealthEndpoint())
                .metricsEndpoint(domain.getMetricsEndpoint())
                .collectionInterval(domain.getCollectionInterval())
                .enabled(domain.getEnabled())
                .tags(domain.getTags())
                .metadata(domain.getMetadata())
                .registeredAt(domain.getRegisteredAt() != null ? domain.getRegisteredAt() : java.time.Instant.now())
                .lastHeartbeat(domain.getLastHeartbeat())
                .status(domain.getStatus() != null ? domain.getStatus().name() : ServiceRegistration.ServiceStatus.REGISTERED.name())
                .build();
    }

    private ServiceRegistration toDomain(ServiceRegistrationDocument document) {
        return ServiceRegistration.builder()
                .serviceId(document.getServiceId())
                .tenantId(document.getTenantId())
                .serviceName(document.getServiceName())
                .serviceType(parseServiceType(document.getServiceType()))
                .category(document.getCategory())
                .version(document.getVersion())
                .description(document.getDescription())
                .baseUrl(document.getBaseUrl())
                .healthEndpoint(document.getHealthEndpoint())
                .metricsEndpoint(document.getMetricsEndpoint())
                .collectionInterval(document.getCollectionInterval())
                .enabled(document.getEnabled() != null ? document.getEnabled() : true)
                .tags(document.getTags())
                .metadata(document.getMetadata())
                .registeredAt(document.getRegisteredAt())
                .lastHeartbeat(document.getLastHeartbeat())
                .status(parseServiceStatus(document.getStatus()))
                .build();
    }

    private MetricDataPoint.ServiceType parseServiceType(String value) {
        if (value == null) return null;
        try {
            return MetricDataPoint.ServiceType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private ServiceRegistration.ServiceStatus parseServiceStatus(String value) {
        if (value == null) return ServiceRegistration.ServiceStatus.REGISTERED;
        try {
            return ServiceRegistration.ServiceStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return ServiceRegistration.ServiceStatus.REGISTERED;
        }
    }
}
