package com.gogidix.dashboard.core.application.service;

import com.gogidix.dashboard.core.application.dto.request.CreateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.request.RecordKPIValueRequestDto;
import com.gogidix.dashboard.core.application.dto.request.UpdateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.response.KPIResponseDto;
import com.gogidix.dashboard.core.application.mapper.KPIMapper;
import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import com.gogidix.dashboard.core.domain.model.KPIValue;
import com.gogidix.dashboard.core.domain.model.SourceDomain;
import com.gogidix.dashboard.core.domain.port.in.CreateKPICommand;
import com.gogidix.dashboard.core.domain.port.in.RecordKPIValueCommand;
import com.gogidix.dashboard.core.domain.port.in.UpdateKPICommand;
import com.gogidix.dashboard.core.domain.port.out.DashboardKPIRepository;
import com.gogidix.dashboard.core.domain.port.out.KPIValueRepository;
import com.gogidix.dashboard.core.infrastructure.messaging.kafka.event.KPIEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Command service for KPI operations.
 * Handles write operations for KPI management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KPICommandService {

    private final DashboardKPIRepository kpiRepository;
    private final KPIValueRepository valueRepository;
    private final KPIMapper mapper;
    private final KPIEventPublisher eventPublisher;

    /**
     * Create a new KPI
     */
    @Transactional
    public KPIResponseDto createKPI(CreateKPIRequestDto request, String tenantId) {
        log.info("Creating KPI: code={}, tenant={}", request.getCode(), tenantId);

        // Check if KPI code already exists
        if (kpiRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("KPI with code " + request.getCode() + " already exists");
        }

        CreateKPICommand command = mapper.toCreateCommand(request, tenantId);
        DashboardKPI kpi = createKPIFromCommand(command);

        DashboardKPI saved = kpiRepository.save(kpi);

        // Publish KPI created event
        eventPublisher.publishKPICreated(saved);

        log.info("KPI created successfully: id={}", saved.getId());
        return mapper.toDto(saved);
    }

    /**
     * Update an existing KPI
     */
    @Transactional
    public KPIResponseDto updateKPI(String kpiId, UpdateKPIRequestDto request) {
        log.info("Updating KPI: id={}", kpiId);

        DashboardKPI kpi = kpiRepository.findById(UUID.fromString(kpiId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + kpiId));

        mapper.updateEntityFromDto(request, kpi);

        DashboardKPI updated = kpiRepository.save(kpi);

        // Publish KPI updated event
        eventPublisher.publishKPIUpdated(updated);

        log.info("KPI updated successfully: id={}", kpiId);
        return mapper.toDto(updated);
    }

    /**
     * Delete a KPI
     */
    @Transactional
    public void deleteKPI(String kpiId) {
        log.info("Deleting KPI: id={}", kpiId);

        DashboardKPI kpi = kpiRepository.findById(UUID.fromString(kpiId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + kpiId));

        // Delete associated values and targets
        valueRepository.deleteByKpiId(kpi.getId());

        kpiRepository.delete(kpi);

        // Publish KPI deleted event
        eventPublisher.publishKPIDeleted(kpi);

        log.info("KPI deleted successfully: id={}", kpiId);
    }

    /**
     * Record a KPI value
     */
    @Transactional
    public KPIResponseDto recordKPIValue(String kpiId, RecordKPIValueRequestDto request) {
        log.info("Recording KPI value: kpiId={}, value={}", kpiId, request.getValue());

        DashboardKPI kpi = kpiRepository.findById(UUID.fromString(kpiId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + kpiId));

        // Update current value
        kpi.updateValue(request.getValue());

        // Create historical value record
        KPIValue value = KPIValue.builder()
                .kpi(kpi)
                .value(request.getValue())
                .recordedAt(request.getRecordedAt())
                .metadata(request.getMetadata())
                .build();

        valueRepository.save(value);
        kpiRepository.save(kpi);

        // Publish KPI value updated event
        eventPublisher.publishKPIValueUpdated(kpi, request.getValue());

        log.info("KPI value recorded successfully: kpiId={}, value={}", kpiId, request.getValue());
        return mapper.toDto(kpi);
    }

    /**
     * Calculate and update KPI value
     */
    @Transactional
    public KPIResponseDto calculateKPI(String kpiCode, String tenantId) {
        log.info("Calculating KPI: code={}, tenant={}", kpiCode, tenantId);

        DashboardKPI kpi = kpiRepository.findByCode(kpiCode)
                .filter(k -> k.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + kpiCode));

        // This would trigger the calculation engine
        // For now, we'll just update the timestamp
        Double calculatedValue = performCalculation(kpi);

        kpi.updateValue(calculatedValue);

        KPIValue value = KPIValue.builder()
                .kpi(kpi)
                .value(calculatedValue)
                .build();

        valueRepository.save(value);
        DashboardKPI saved = kpiRepository.save(kpi);

        // Publish KPI calculated event
        eventPublisher.publishKPICalculated(saved, calculatedValue);

        log.info("KPI calculated successfully: code={}, value={}", kpiCode, calculatedValue);
        return mapper.toDto(saved);
    }

    private DashboardKPI createKPIFromCommand(CreateKPICommand command) {
        return DashboardKPI.builder()
                .name(command.getName())
                .description(command.getDescription())
                .code(command.getCode())
                .category(command.getCategory())
                .tenantId(command.getTenantId())
                .sourceDomain(SourceDomain.valueOf(command.getSourceDomain()))
                .unit(command.getUnit())
                .dataType(command.getDataType())
                .aggregationType(command.getAggregationType())
                .formula(command.getFormula())
                .isActive(command.getIsActive())
                .isRealTime(command.getIsRealTime())
                .refreshIntervalSeconds(command.getRefreshIntervalSeconds())
                .thresholdWarning(command.getThresholdWarning())
                .thresholdCritical(command.getThresholdCritical())
                .targetValue(command.getTargetValue())
                .createdBy(command.getCreatedBy())
                .build();
    }

    private Double performCalculation(DashboardKPI kpi) {
        // This is a placeholder for the actual calculation logic
        // In a real implementation, this would:
        // 1. Parse the formula
        // 2. Fetch data from source domains
        // 3. Apply aggregation
        // 4. Return the calculated value

        // For now, return a mock value
        return kpi.getCurrentValue() != null ? kpi.getCurrentValue() + 1.0 : 0.0;
    }
}
