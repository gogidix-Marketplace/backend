package com.gogidix.monitoring.monitoringdataservice.application.service;

import com.gogidix.monitoring.monitoringdataservice.application.dto.CollectBatchMetricsRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.CollectMetricRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricDataPointResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricQueryResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.QueryMetricsRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.mapper.MetricDataPointMapper;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricAggregation;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricAggregationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricDataPointRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricEventPublisherPort;
import com.gogidix.monitoring.monitoringdataservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for metric collection and querying.
 */
@Service
@Transactional(readOnly = true)
public class MetricCollectionService {

    private static final Logger log = LoggerFactory.getLogger(MetricCollectionService.class);

    private final MetricDataPointRepositoryPort metricRepository;
    private final MetricAggregationRepositoryPort aggregationRepository;
    private final MetricEventPublisherPort eventPublisher;
    private final MetricDataPointMapper mapper;

    public MetricCollectionService(
            MetricDataPointRepositoryPort metricRepository,
            MetricAggregationRepositoryPort aggregationRepository,
            MetricEventPublisherPort eventPublisher,
            MetricDataPointMapper mapper) {
        this.metricRepository = metricRepository;
        this.aggregationRepository = aggregationRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    /**
     * Collect a single metric.
     */
    @Transactional
    public MetricDataPointResponseDto collectMetric(String tenantId, CollectMetricRequestDto request) {
        log.debug("Collecting metric: {} for service: {}", request.getMetricName(), request.getServiceName());

        MetricDataPoint dataPoint = buildMetricDataPoint(tenantId, request);
        validateMetric(dataPoint);

        MetricDataPoint saved = metricRepository.save(dataPoint);

        // Publish event asynchronously
        eventPublisher.publishMetric(saved);

        // Check threshold
        checkThreshold(saved);

        return mapper.toResponseDto(saved);
    }

    /**
     * Collect multiple metrics in batch.
     */
    @Transactional
    public List<MetricDataPointResponseDto> collectBatchMetrics(String tenantId, CollectBatchMetricsRequestDto request) {
        log.debug("Collecting {} metrics", request.getMetrics().size());

        List<MetricDataPoint> dataPoints = request.getMetrics().stream()
                .map(dto -> buildMetricDataPoint(tenantId, dto))
                .peek(this::validateMetric)
                .toList();

        long savedCount = metricRepository.saveAll(dataPoints);

        // Publish events
        eventPublisher.publishMetrics(dataPoints);

        log.info("Collected {} metrics", savedCount);

        return dataPoints.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Query metrics with optional aggregation.
     */
    @Cacheable(value = "metricQueries", key = "#tenantId + ':' + #request.serviceName + ':' + #request.metricName + ':' + #request.startTime + ':' + #request.endTime")
    public MetricQueryResponseDto queryMetrics(String tenantId, QueryMetricsRequestDto request) {
        log.debug("Querying metrics for service: {} from {} to {}",
                request.getServiceName(), request.getStartTime(), request.getEndTime());

        List<MetricDataPoint> dataPoints;
        if (request.getMetricName() != null && !request.getMetricName().isBlank()) {
            dataPoints = metricRepository.findByServiceMetricAndTimeRange(
                    tenantId,
                    request.getServiceName(),
                    request.getMetricName(),
                    request.getStartTime(),
                    request.getEndTime()
            );
        } else {
            dataPoints = metricRepository.findByServiceAndTimeRange(
                    tenantId,
                    request.getServiceName(),
                    request.getStartTime(),
                    request.getEndTime()
            );
        }

        // Calculate statistics
        MetricQueryResponseDto.MetricStatistics statistics = calculateStatistics(dataPoints);

        // Build response
        return MetricQueryResponseDto.builder()
                .serviceName(request.getServiceName())
                .metricName(request.getMetricName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .dataPointCount((long) dataPoints.size())
                .statistics(statistics)
                .dataPoints(dataPoints.stream()
                        .sorted(Comparator.comparing(MetricDataPoint::getTimestamp))
                        .limit(1000) // Limit response size
                        .map(dp -> MetricQueryResponseDto.DataPoint.builder()
                                .timestamp(dp.getTimestamp())
                                .value(dp.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Get aggregated metrics.
     */
    public MetricQueryResponseDto getAggregatedMetrics(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window,
            Instant startTime,
            Instant endTime
    ) {
        log.debug("Getting {} aggregations for service: {} metric: {}", window, serviceName, metricName);

        List<MetricAggregation> aggregations = aggregationRepository
                .findByServiceMetricAndTimeRange(tenantId, serviceName, metricName, window, startTime, endTime);

        if (aggregations.isEmpty()) {
            return MetricQueryResponseDto.builder()
                    .serviceName(serviceName)
                    .metricName(metricName)
                    .startTime(startTime)
                    .endTime(endTime)
                    .dataPointCount(0L)
                    .dataPoints(new ArrayList<>())
                    .build();
        }

        // Calculate overall statistics from aggregations
        DoubleSummaryStatistics stats = aggregations.stream()
                .filter(a -> a.getAvg() != null)
                .mapToDouble(MetricAggregation::getAvg)
                .summaryStatistics();

        MetricQueryResponseDto.MetricStatistics statistics = MetricQueryResponseDto.MetricStatistics.builder()
                .min(aggregations.stream().map(MetricAggregation::getMin).min(Double::compare).orElse(null))
                .max(aggregations.stream().map(MetricAggregation::getMax).max(Double::compare).orElse(null))
                .avg(stats.getAverage())
                .sum(stats.getSum())
                .build();

        return MetricQueryResponseDto.builder()
                .serviceName(serviceName)
                .metricName(metricName)
                .startTime(startTime)
                .endTime(endTime)
                .dataPointCount((long) aggregations.size())
                .statistics(statistics)
                .dataPoints(aggregations.stream()
                        .sorted(Comparator.comparing(MetricAggregation::getWindowStart))
                        .map(a -> MetricQueryResponseDto.DataPoint.builder()
                                .timestamp(a.getWindowStart())
                                .value(a.getAvg())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Get metric count by tenant.
     */
    public long getMetricCount(String tenantId) {
        return metricRepository.countByTenant(tenantId);
    }

    private MetricDataPoint buildMetricDataPoint(String tenantId, CollectMetricRequestDto request) {
        Instant timestamp = request.getTimestamp() != null
                ? Instant.ofEpochMilli(request.getTimestamp())
                : Instant.now();

        return MetricDataPoint.builder()
                .tenantId(tenantId)
                .serviceName(request.getServiceName())
                .serviceType(parseServiceType(request.getServiceType()))
                .metricName(request.getMetricName())
                .value(request.getValue())
                .unit(request.getUnit())
                .metricType(parseMetricType(request.getMetricType()))
                .tags(request.getTags())
                .timestamp(timestamp)
                .host(request.getHost())
                .instanceId(request.getInstanceId())
                .correlationId(request.getCorrelationId())
                .category(request.getCategory())
                .createdAt(Instant.now())
                .build();
    }

    private void validateMetric(MetricDataPoint dataPoint) {
        if (!dataPoint.isValid()) {
            throw new ValidationException("Invalid metric data point");
        }
    }

    private void checkThreshold(MetricDataPoint dataPoint) {
        // Basic threshold checking - can be enhanced with configurable thresholds
        if ("cpu.usage".equals(dataPoint.getMetricName()) && dataPoint.getValue() > 90.0) {
            eventPublisher.publishThresholdExceeded(
                    dataPoint.getTenantId(),
                    dataPoint.getServiceName(),
                    dataPoint.getMetricName(),
                    90.0,
                    dataPoint.getValue(),
                    "CPU usage exceeds 90%"
            );
        }

        if ("memory.usage".equals(dataPoint.getMetricName()) && dataPoint.getValue() > 90.0) {
            eventPublisher.publishThresholdExceeded(
                    dataPoint.getTenantId(),
                    dataPoint.getServiceName(),
                    dataPoint.getMetricName(),
                    90.0,
                    dataPoint.getValue(),
                    "Memory usage exceeds 90%"
            );
        }
    }

    private MetricQueryResponseDto.MetricStatistics calculateStatistics(List<MetricDataPoint> dataPoints) {
        if (dataPoints.isEmpty()) {
            return MetricQueryResponseDto.MetricStatistics.builder().build();
        }

        DoubleSummaryStatistics stats = dataPoints.stream()
                .mapToDouble(MetricDataPoint::getValue)
                .summaryStatistics();

        List<Double> sortedValues = dataPoints.stream()
                .map(MetricDataPoint::getValue)
                .sorted()
                .toList();

        return MetricQueryResponseDto.MetricStatistics.builder()
                .min(stats.getMin())
                .max(stats.getMax())
                .avg(stats.getAverage())
                .sum(stats.getSum())
                .p50(calculatePercentile(sortedValues, 50))
                .p95(calculatePercentile(sortedValues, 95))
                .p99(calculatePercentile(sortedValues, 99))
                .build();
    }

    private Double calculatePercentile(List<Double> sortedValues, int percentile) {
        if (sortedValues.isEmpty()) {
            return null;
        }
        int index = (int) Math.ceil((percentile / 100.0) * sortedValues.size()) - 1;
        return sortedValues.get(Math.max(0, Math.min(index, sortedValues.size() - 1)));
    }

    private MetricDataPoint.ServiceType parseServiceType(String value) {
        if (value == null) return null;
        try {
            return MetricDataPoint.ServiceType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private MetricDataPoint.MetricType parseMetricType(String value) {
        if (value == null) return null;
        try {
            return MetricDataPoint.MetricType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
