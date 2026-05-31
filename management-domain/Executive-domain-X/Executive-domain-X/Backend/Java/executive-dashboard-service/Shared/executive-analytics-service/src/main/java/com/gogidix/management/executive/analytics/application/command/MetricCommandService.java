package com.gogidix.management.executive.analytics.application.command;

import com.gogidix.management.executive.analytics.application.dto.CreateMetricRequest;
import com.gogidix.management.executive.analytics.domain.model.Metric;
import com.gogidix.management.executive.analytics.domain.repository.MetricRepository;
import com.gogidix.management.shared.exception.NotFoundException;
import com.gogidix.management.shared.exception.ValidationException;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricCommandService {
    private final MetricRepository metricRepository;

    @Transactional
    @CacheEvict(value = "metrics", allEntries = true)
    public Metric createMetric(CreateMetricRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating metric for tenant: {}, name: {}", tenantId, request.getMetricName());
        validateRequest(request);
        BigDecimal value = convertToBigDecimal(request.getValue());
        Metric metric = new Metric(tenantId, request.getMetricName(), request.getSourceDomain(),
            value, request.getTimestamp() != null ? request.getTimestamp() : Instant.now());
        metric.setSourceService(request.getSourceService());
        metric.setUnit(request.getUnit());
        metric.setGranularity(request.getGranularity() != null ? request.getGranularity() : "DAY");
        metric.setDimensions(request.getDimensions());
        metric.setMetadata(request.getMetadata());
        metric.setQualityScore(request.getQualityScore() != null ? BigDecimal.valueOf(request.getQualityScore()) : BigDecimal.ONE);
        metric.setIsAggregate(request.getIsAggregate() != null ? request.getIsAggregate() : false);
        metric.setParentMetricId(request.getParentMetricId());
        Metric saved = metricRepository.save(metric);
        log.debug("Metric created: {}", saved.getId());
        return saved;
    }

    @Transactional
    @CacheEvict(value = "metrics", allEntries = true)
    public List<Metric> bulkCreateMetrics(List<CreateMetricRequest> requests) {
        log.info("Bulk creating {} metrics", requests.size());
        return requests.stream()
            .map(this::createMetric)
            .toList();
    }

    @Transactional
    @CachePut(value = "metric", key = "#id")
    public Metric updateQualityScore(String id, Double qualityScore) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating quality score for metric: {} = {}", id, qualityScore);
        Metric metric = metricRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ValidationException("metricId", "Metric not found: " + id));
        metric.updateQualityScore(BigDecimal.valueOf(qualityScore));
        metric.touch();
        return metricRepository.save(metric);
    }

    @Transactional
    @CacheEvict(value = "metric", key = "#id")
    public void markAsAggregate(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Marking metric as aggregate: {}", id);
        Metric metric = metricRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("Metric", id));
        metric.markAsAggregate();
        metricRepository.save(metric);
    }

    @Transactional
    public long deleteOlderThan(Instant threshold) {
        log.info("Deleting metrics older than {}", threshold);
        return metricRepository.deleteOlderThan(threshold);
    }

    private void validateRequest(CreateMetricRequest request) {
        if (request.getMetricName() == null || request.getMetricName().isBlank()) {
            throw new ValidationException("metricName", "Metric name is required");
        }
        if (request.getSourceDomain() == null || request.getSourceDomain().isBlank()) {
            throw new ValidationException("sourceDomain", "Source domain is required");
        }
        if (request.getValue() == null) {
            throw new ValidationException("value", "Value is required");
        }
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                throw new ValidationException("value", "Cannot convert '" + value + "' to number");
            }
        }
        throw new ValidationException("value", "Unsupported value type: " + value.getClass());
    }
}
