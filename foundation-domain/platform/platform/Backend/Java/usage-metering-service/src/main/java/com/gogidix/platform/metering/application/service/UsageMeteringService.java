package com.gogidix.platform.metering.application.service;

import com.gogidix.platform.metering.application.dto.CreateUsageRecordRequestDto;
import com.gogidix.platform.metering.application.dto.UsageRecordDto;
import com.gogidix.platform.metering.domain.model.UsageRecord;
import com.gogidix.platform.metering.domain.repository.UsageRecordRepository;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for usage metering operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsageMeteringService {

    private final UsageRecordRepository usageRecordRepository;

    /**
     * Record usage event
     */
    @Transactional
    public UsageRecordDto recordUsage(CreateUsageRecordRequestDto request) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        log.info("Recording usage: tenantId={}, metric={}, quantity={}",
            tenantId, request.getMetricName(), request.getQuantity());

        UsageRecord record = UsageRecord.builder()
            .tenantId(tenantId)
            .metricName(request.getMetricName())
            .metricType(UsageRecord.MetricType.COUNTER) // Default type
            .quantity(request.getQuantity())
            .unit(request.getUnit())
            .eventTime(request.getEventTime())
            .receivedAt(LocalDateTime.now())
            .dimensions(request.getDimensions() != null ? request.getDimensions() : new java.util.HashMap<>())
            .serviceName(request.getServiceName())
            .resourceId(request.getResourceId())
            .userId(request.getUserId())
            .correlationId(request.getCorrelationId())
            .metadata(request.getMetadata() != null ? request.getMetadata() : new java.util.HashMap<>())
            .build();

        UsageRecord saved = usageRecordRepository.save(record);
        log.info("Usage recorded successfully: id={}", saved.getId());

        return toDto(saved);
    }

    /**
     * Get usage records by tenant and time range
     */
    public List<UsageRecordDto> getUsageByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return usageRecordRepository.findByTenantIdAndTimeRange(tenantId, startTime, endTime).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get usage records by tenant
     */
    public List<UsageRecordDto> getUsageByTenant() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return usageRecordRepository.findByTenantId(tenantId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private UsageRecordDto toDto(UsageRecord record) {
        return UsageRecordDto.builder()
            .id(record.getId())
            .tenantId(record.getTenantId())
            .metricName(record.getMetricName())
            .metricType(record.getMetricType().name())
            .quantity(record.getQuantity())
            .unit(record.getUnit())
            .eventTime(record.getEventTime())
            .receivedAt(record.getReceivedAt())
            .dimensions(record.getDimensions())
            .serviceName(record.getServiceName())
            .resourceId(record.getResourceId())
            .userId(record.getUserId())
            .correlationId(record.getCorrelationId())
            .metadata(record.getMetadata())
            .build();
    }
}
