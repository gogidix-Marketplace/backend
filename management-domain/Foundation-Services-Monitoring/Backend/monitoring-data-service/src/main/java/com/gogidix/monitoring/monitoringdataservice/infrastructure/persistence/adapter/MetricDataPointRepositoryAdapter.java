package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.adapter;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricDataPointRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.MetricDataPointDocument;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository.SpringDataMetricDataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB adapter for metric data point repository.
 */
@Repository
public class MetricDataPointRepositoryAdapter implements MetricDataPointRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(MetricDataPointRepositoryAdapter.class);

    private final SpringDataMetricDataPointRepository springRepository;

    public MetricDataPointRepositoryAdapter(SpringDataMetricDataPointRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public MetricDataPoint save(MetricDataPoint dataPoint) {
        log.debug("Saving metric data point: {} for service: {}", dataPoint.getMetricName(), dataPoint.getServiceName());

        MetricDataPointDocument document = toDocument(dataPoint);
        MetricDataPointDocument saved = springRepository.save(document);

        return toDomain(saved);
    }

    @Override
    public long saveAll(List<MetricDataPoint> dataPoints) {
        log.debug("Saving {} metric data points", dataPoints.size());

        List<MetricDataPointDocument> documents = dataPoints.stream()
                .map(this::toDocument)
                .toList();

        List<MetricDataPointDocument> saved = springRepository.saveAll(documents);

        log.debug("Saved {} metric data points", saved.size());
        return saved.size();
    }

    @Override
    public Optional<MetricDataPoint> findById(String id) {
        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<MetricDataPoint> findByServiceAndTimeRange(
            String tenantId,
            String serviceName,
            Instant startTime,
            Instant endTime
    ) {
        List<MetricDataPointDocument> documents = springRepository
                .findByTenantIdAndServiceNameAndTimestampBetween(
                        tenantId, serviceName, startTime, endTime);

        return documents.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<MetricDataPoint> findByServiceMetricAndTimeRange(
            String tenantId,
            String serviceName,
            String metricName,
            Instant startTime,
            Instant endTime
    ) {
        List<MetricDataPointDocument> documents = springRepository
                .findByTenantIdAndServiceNameAndMetricNameAndTimestampBetween(
                        tenantId, serviceName, metricName, startTime, endTime);

        return documents.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long deleteOlderThan(Instant beforeTimestamp) {
        long deleted = springRepository.deleteByTimestampBefore(beforeTimestamp);
        log.info("Deleted {} metric data points older than {}", deleted, beforeTimestamp);
        return deleted;
    }

    @Override
    public long countByTenant(String tenantId) {
        return springRepository.countByTenantId(tenantId);
    }

    @Override
    public Optional<Instant> getLatestMetricTimestamp(String tenantId, String serviceName) {
        List<MetricDataPointDocument> latest = springRepository
                .findLatestByTenantAndService(tenantId, serviceName);

        return latest.isEmpty() ? Optional.empty() : Optional.of(latest.get(0).getTimestamp());
    }

    private MetricDataPointDocument toDocument(MetricDataPoint domain) {
        return MetricDataPointDocument.builder()
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
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt() : Instant.now())
                .build();
    }

    private MetricDataPoint toDomain(MetricDataPointDocument document) {
        return MetricDataPoint.builder()
                .id(document.getId())
                .tenantId(document.getTenantId())
                .serviceName(document.getServiceName())
                .serviceType(parseServiceType(document.getServiceType()))
                .metricName(document.getMetricName())
                .value(document.getValue())
                .unit(document.getUnit())
                .metricType(parseMetricType(document.getMetricType()))
                .tags(document.getTags())
                .timestamp(document.getTimestamp())
                .host(document.getHost())
                .instanceId(document.getInstanceId())
                .correlationId(document.getCorrelationId())
                .category(document.getCategory())
                .createdAt(document.getCreatedAt())
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

    private MetricDataPoint.MetricType parseMetricType(String value) {
        if (value == null) return null;
        try {
            return MetricDataPoint.MetricType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
