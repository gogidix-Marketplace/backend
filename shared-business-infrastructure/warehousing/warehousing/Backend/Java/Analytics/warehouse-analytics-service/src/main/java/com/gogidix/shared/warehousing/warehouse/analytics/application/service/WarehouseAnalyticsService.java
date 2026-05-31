package com.gogidix.shared.warehousing.warehouse.analytics.application.service;

import com.gogidix.shared.warehousing.warehouse.analytics.application.command.CreateMetricsCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.command.GenerateReportCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.PerformanceDataDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.UtilizationReportDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.WarehouseMetricsDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.mapper.WarehouseAnalyticsMapper;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.PerformanceData;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.UtilizationReport;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.WarehouseMetrics;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.events.MetricsGeneratedEvent;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.events.ReportGeneratedEvent;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.exception.EntityNotFoundException;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.repository.PerformanceDataRepository;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.repository.UtilizationReportRepository;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.repository.WarehouseMetricsRepository;
import com.gogidix.shared.warehousing.warehouse.analytics.infrastructure.messaging.WarehouseAnalyticsEventPublisher;
import com.gogidix.shared.warehousing.warehouse.analytics.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Warehouse Analytics Application Service
 *
 * Handles warehouse analytics operations with multi-tenant support
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseAnalyticsService {

    private final WarehouseMetricsRepository metricsRepository;
    private final UtilizationReportRepository reportRepository;
    private final PerformanceDataRepository performanceRepository;
    private final WarehouseAnalyticsMapper mapper;
    private final WarehouseAnalyticsEventPublisher eventPublisher;

    /**
     * Create warehouse metrics
     */
    public WarehouseMetricsDTO createMetrics(CreateMetricsCommand command) {
        log.info("Creating metrics for warehouse: {}", command.getWarehouseId());

        String tenantId = TenantContext.getCurrentTenantId();

        WarehouseMetrics metrics = mapper.toEntity(command);
        metrics.setTenantId(tenantId);

        // Calculate derived values
        if (command.getTotalCapacity() != null && command.getUsedCapacity() != null) {
            metrics.setAvailableCapacity(command.getTotalCapacity() - command.getUsedCapacity());
            metrics.setUtilizationPercentage((command.getUsedCapacity() / command.getTotalCapacity()) * 100);
        }

        if (command.getOrdersProcessed() != null) {
            metrics.setOrdersPerHour((double) command.getOrdersProcessed() / 24); // Daily average
        }

        WarehouseMetrics savedMetrics = metricsRepository.save(metrics);

        // Publish event
        MetricsGeneratedEvent event = MetricsGeneratedEvent.builder()
            .metricsId(savedMetrics.getId())
            .warehouseId(savedMetrics.getWarehouseId())
            .tenantId(savedMetrics.getTenantId())
            .utilizationPercentage(savedMetrics.getUtilizationPercentage())
            .ordersProcessed(savedMetrics.getOrdersProcessed())
            .metricDate(savedMetrics.getMetricDate())
            .build();
        eventPublisher.publishMetricsGenerated(event);

        log.info("Metrics created with ID: {}", savedMetrics.getId());
        return mapper.toDTO(savedMetrics);
    }

    /**
     * Get metrics by ID
     */
    @Transactional(readOnly = true)
    public WarehouseMetricsDTO getMetrics(String id) {
        WarehouseMetrics metrics = metricsRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Metrics not found: " + id));
        return mapper.toDTO(metrics);
    }

    /**
     * Get metrics by warehouse
     */
    @Transactional(readOnly = true)
    public List<WarehouseMetricsDTO> getMetricsByWarehouse(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<WarehouseMetrics> metrics = metricsRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return mapper.toMetricsDTOList(metrics);
    }

    /**
     * Get all metrics for current tenant
     */
    @Transactional(readOnly = true)
    public List<WarehouseMetricsDTO> getAllMetrics() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<WarehouseMetrics> metrics = metricsRepository.findByTenantId(tenantId);
        return mapper.toMetricsDTOList(metrics);
    }

    /**
     * Get latest metrics for a warehouse
     */
    @Transactional(readOnly = true)
    public WarehouseMetricsDTO getLatestMetrics(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        WarehouseMetrics metrics = metricsRepository
            .findFirstByTenantIdAndWarehouseIdOrderByMetricDateDesc(tenantId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("No metrics found for warehouse: " + warehouseId));
        return mapper.toDTO(metrics);
    }

    /**
     * Get utilization for a warehouse
     */
    @Transactional(readOnly = true)
    public Double getWarehouseUtilization(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        WarehouseMetrics metrics = metricsRepository
            .findFirstByTenantIdAndWarehouseIdOrderByMetricDateDesc(tenantId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("No metrics found for warehouse: " + warehouseId));
        return metrics.getUtilizationPercentage();
    }

    /**
     * Generate utilization report
     */
    public UtilizationReportDTO generateReport(GenerateReportCommand command) {
        log.info("Generating report for warehouse: {}", command.getWarehouseId());

        String tenantId = TenantContext.getCurrentTenantId();

        UtilizationReport report = UtilizationReport.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .warehouseName(command.getWarehouseName())
            .reportDate(LocalDateTime.now())
            .periodStart(command.getPeriodStart())
            .periodEnd(command.getPeriodEnd())
            .zoneUtilizations(new ArrayList<>())
            .overallSpaceUtilization(0.0)
            .overallEquipmentUtilization(0.0)
            .overallLaborUtilization(0.0)
            .utilizationTrend(0.0)
            .trendDirection("STABLE")
            .recommendations(new ArrayList<>())
            .status(UtilizationReport.ReportStatus.COMPLETED)
            .generatedBy(command.getGeneratedBy())
            .build();

        UtilizationReport savedReport = reportRepository.save(report);

        // Publish event
        ReportGeneratedEvent event = ReportGeneratedEvent.builder()
            .reportId(savedReport.getId())
            .warehouseId(savedReport.getWarehouseId())
            .tenantId(savedReport.getTenantId())
            .reportDate(savedReport.getReportDate())
            .overallSpaceUtilization(savedReport.getOverallSpaceUtilization())
            .status(savedReport.getStatus().name())
            .build();
        eventPublisher.publishReportGenerated(event);

        log.info("Report generated with ID: {}", savedReport.getId());
        return mapper.toReportDTO(savedReport);
    }

    /**
     * Get report by ID
     */
    @Transactional(readOnly = true)
    public UtilizationReportDTO getReport(String id) {
        UtilizationReport report = reportRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Report not found: " + id));
        return mapper.toReportDTO(report);
    }

    /**
     * Get reports by warehouse
     */
    @Transactional(readOnly = true)
    public List<UtilizationReportDTO> getReportsByWarehouse(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<UtilizationReport> reports = reportRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return mapper.toReportDTOList(reports);
    }

    /**
     * Get performance data by warehouse
     */
    @Transactional(readOnly = true)
    public List<PerformanceDataDTO> getPerformanceData(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PerformanceData> performance = performanceRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return mapper.toPerformanceDTOList(performance);
    }

    /**
     * Get latest performance data
     */
    @Transactional(readOnly = true)
    public PerformanceDataDTO getLatestPerformanceData(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        PerformanceData performance = performanceRepository
            .findFirstByTenantIdAndWarehouseIdOrderByTimestampDesc(tenantId, warehouseId)
            .orElseThrow(() -> new EntityNotFoundException("No performance data found for warehouse: " + warehouseId));
        return mapper.toPerformanceDTO(performance);
    }

    /**
     * Create performance data entry
     */
    public PerformanceDataDTO createPerformanceData(PerformanceData performanceData) {
        log.info("Creating performance data for warehouse: {}", performanceData.getWarehouseId());

        String tenantId = TenantContext.getCurrentTenantId();
        performanceData.setTenantId(tenantId);
        performanceData.setId(UUID.randomUUID().toString());

        PerformanceData saved = performanceRepository.save(performanceData);
        return mapper.toPerformanceDTO(saved);
    }
}
