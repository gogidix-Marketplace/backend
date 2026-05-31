package com.gogidix.monitoring.monitoringdataservice.application.mapper;

import com.gogidix.monitoring.monitoringdataservice.application.dto.ServiceRegistrationResponseDto;
import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;
import org.springframework.stereotype.Component;

/**
 * Mapper for service registration entities.
 */
@Component
public class ServiceRegistrationMapper {

    public ServiceRegistrationResponseDto toResponseDto(ServiceRegistration domain) {
        return ServiceRegistrationResponseDto.builder()
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
                .registeredAt(domain.getRegisteredAt())
                .lastHeartbeat(domain.getLastHeartbeat())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .build();
    }
}
