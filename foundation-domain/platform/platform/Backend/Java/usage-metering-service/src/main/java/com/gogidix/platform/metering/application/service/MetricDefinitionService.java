package com.gogidix.platform.metering.application.service;

import com.gogidix.platform.metering.application.dto.MetricDefinitionDto;
import com.gogidix.platform.metering.domain.model.MetricDefinition;
import com.gogidix.platform.metering.domain.repository.MetricDefinitionRepository;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for metric definition operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricDefinitionService {

    private final MetricDefinitionRepository metricDefinitionRepository;

    /**
     * Create metric definition
     */
    @Transactional
    @CacheEvict(value = "metrics", allEntries = true)
    public MetricDefinitionDto createMetricDefinition(MetricDefinitionDto dto) {
        log.info("Creating metric definition: name={}", dto.getMetricName());

        if (metricDefinitionRepository.existsByMetricName(dto.getMetricName())) {
            throw new ConflictException("Metric already exists: " + dto.getMetricName());
        }

        MetricDefinition metric = MetricDefinition.builder()
            .metricName(dto.getMetricName())
            .metricDisplayName(dto.getMetricDisplayName())
            .description(dto.getDescription())
            .metricType(dto.getMetricType() != null ? MetricDefinition.MetricType.valueOf(dto.getMetricType()) : MetricDefinition.MetricType.COUNTER)
            .metricCategory(dto.getMetricCategory())
            .unit(dto.getUnit())
            .aggregationType(dto.getAggregationType() != null ? MetricDefinition.AggregationType.valueOf(dto.getAggregationType()) : MetricDefinition.AggregationType.SUM)
            .retentionDays(dto.getRetentionDays() != null ? dto.getRetentionDays() : 90)
            .billable(dto.isBillable())
            .unitPrice(dto.getUnitPrice())
            .dimensions(dto.getDimensions() != null ? dto.getDimensions() : new String[]{})
            .isActive(true)
            .build();

        MetricDefinition saved = metricDefinitionRepository.save(metric);
        return toDto(saved);
    }

    /**
     * Get metric definition by name
     */
    @Cacheable(value = "metrics", key = "#metricName")
    public MetricDefinitionDto getMetricDefinition(String metricName) {
        MetricDefinition metric = metricDefinitionRepository.findByMetricName(metricName)
            .orElseThrow(() -> new NotFoundException("Metric not found: " + metricName));
        return toDto(metric);
    }

    /**
     * Get all active metrics
     */
    public List<MetricDefinitionDto> getActiveMetrics() {
        return metricDefinitionRepository.findByIsActive(true).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all metrics
     */
    public List<MetricDefinitionDto> getAllMetrics() {
        return metricDefinitionRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private MetricDefinitionDto toDto(MetricDefinition metric) {
        return MetricDefinitionDto.builder()
            .id(metric.getId())
            .metricName(metric.getMetricName())
            .metricDisplayName(metric.getMetricDisplayName())
            .description(metric.getDescription())
            .metricType(metric.getMetricType().name())
            .metricCategory(metric.getMetricCategory())
            .unit(metric.getUnit())
            .aggregationType(metric.getAggregationType() != null ? metric.getAggregationType().name() : null)
            .retentionDays(metric.getRetentionDays())
            .billable(metric.isBillable())
            .unitPrice(metric.getUnitPrice())
            .dimensions(metric.getDimensions())
            .isActive(metric.isActive())
            .createdAt(metric.getCreatedAt())
            .updatedAt(metric.getUpdatedAt())
            .build();
    }
}
