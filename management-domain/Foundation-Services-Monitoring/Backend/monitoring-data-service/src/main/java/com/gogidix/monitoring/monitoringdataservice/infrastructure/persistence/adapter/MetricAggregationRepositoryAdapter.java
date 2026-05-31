package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.adapter;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricAggregation;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricAggregationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document.MetricAggregationDocument;
import com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.repository.SpringDataMetricAggregationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB adapter for metric aggregation repository.
 */
@Repository
public class MetricAggregationRepositoryAdapter implements MetricAggregationRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(MetricAggregationRepositoryAdapter.class);

    private final SpringDataMetricAggregationRepository springRepository;

    public MetricAggregationRepositoryAdapter(SpringDataMetricAggregationRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public MetricAggregation save(MetricAggregation aggregation) {
        log.debug("Saving metric aggregation: {} for service: {}", aggregation.getMetricName(), aggregation.getServiceName());

        MetricAggregationDocument document = toDocument(aggregation);
        MetricAggregationDocument saved = springRepository.save(document);

        return toDomain(saved);
    }

    @Override
    public List<MetricAggregation> findByServiceMetricAndTimeRange(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window,
            Instant startTime,
            Instant endTime
    ) {
        List<MetricAggregationDocument> documents = springRepository
                .findByTenantIdAndServiceNameAndMetricNameAndWindowAndWindowStartBetween(
                        tenantId, serviceName, metricName, window.name(), startTime, endTime);

        return documents.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<MetricAggregation> findLatest(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window
    ) {
        List<MetricAggregationDocument> documents = springRepository
                .findLatest(tenantId, serviceName, metricName, window.name());

        return documents.isEmpty() ? Optional.empty() : Optional.of(toDomain(documents.get(0)));
    }

    @Override
    public long deleteOlderThan(Instant beforeTimestamp) {
        long deleted = springRepository.deleteByWindowStartBefore(beforeTimestamp);
        log.info("Deleted {} metric aggregations older than {}", deleted, beforeTimestamp);
        return deleted;
    }

    @Override
    public long deleteByWindowAndOlderThan(MetricAggregation.AggregationWindow window, Instant beforeTimestamp) {
        long deleted = springRepository.deleteByWindowAndWindowStartBefore(window.name(), beforeTimestamp);
        log.info("Deleted {} {} aggregations older than {}", deleted, window, beforeTimestamp);
        return deleted;
    }

    private MetricAggregationDocument toDocument(MetricAggregation domain) {
        return MetricAggregationDocument.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .serviceName(domain.getServiceName())
                .serviceType(domain.getServiceType() != null ? domain.getServiceType().name() : null)
                .metricName(domain.getMetricName())
                .window(domain.getWindow() != null ? domain.getWindow().name() : null)
                .windowStart(domain.getWindowStart())
                .windowEnd(domain.getWindowEnd())
                .count(domain.getCount())
                .min(domain.getMin())
                .max(domain.getMax())
                .avg(domain.getAvg())
                .sum(domain.getSum())
                .p50(domain.getP50())
                .p95(domain.getP95())
                .p99(domain.getP99())
                .stdDev(domain.getStdDev())
                .tags(domain.getTags())
                .computedAt(domain.getComputedAt() != null ? domain.getComputedAt() : Instant.now())
                .build();
    }

    private MetricAggregation toDomain(MetricAggregationDocument document) {
        return MetricAggregation.builder()
                .id(document.getId())
                .tenantId(document.getTenantId())
                .serviceName(document.getServiceName())
                .serviceType(parseServiceType(document.getServiceType()))
                .metricName(document.getMetricName())
                .window(parseAggregationWindow(document.getWindow()))
                .windowStart(document.getWindowStart())
                .windowEnd(document.getWindowEnd())
                .count(document.getCount())
                .min(document.getMin())
                .max(document.getMax())
                .avg(document.getAvg())
                .sum(document.getSum())
                .p50(document.getP50())
                .p95(document.getP95())
                .p99(document.getP99())
                .stdDev(document.getStdDev())
                .tags(document.getTags())
                .computedAt(document.getComputedAt())
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

    private MetricAggregation.AggregationWindow parseAggregationWindow(String value) {
        if (value == null) return null;
        try {
            return MetricAggregation.AggregationWindow.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
