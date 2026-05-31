package com.gogidix.monitoring.monitoringdataservice.application.mapper;

import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricDataPointResponseDto;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import org.springframework.stereotype.Component;

/**
 * Mapper for metric data point entities.
 */
@Component
public class MetricDataPointMapper {

    public MetricDataPointResponseDto toResponseDto(MetricDataPoint domain) {
        return MetricDataPointResponseDto.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .serviceName(domain.getServiceName())
                .serviceType(domain.getServiceType() != null ? domain.getServiceType().name() : null)
                .metricName(domain.getMetricName())
                .value(domain.getValue())
                .unit(domain.getUnit())
                .metricType(domain.getMetricType() != null ? domain.getMetricType().name() : null)
                .tags(domain.getTags())
                .timestamp(domain.getTimestamp())
                .host(domain.getHost())
                .instanceId(domain.getInstanceId())
                .correlationId(domain.getCorrelationId())
                .category(domain.getCategory())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
