package com.gogidix.customersupport.supportanalytics.application.service;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportRequestDto;
import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportResponseDto;
import com.gogidix.customersupport.supportanalytics.application.dto.ChannelPerformanceResponseDto;
import com.gogidix.customersupport.supportanalytics.application.dto.TicketTrendResponseDto;
import com.gogidix.customersupport.supportanalytics.application.mapper.AnalyticsReportMapper;
import com.gogidix.customersupport.supportanalytics.application.mapper.ChannelPerformanceMapper;
import com.gogidix.customersupport.supportanalytics.application.mapper.TicketTrendMapper;
import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
import com.gogidix.customersupport.supportanalytics.domain.model.ChannelPerformance;
import com.gogidix.customersupport.supportanalytics.domain.model.TicketTrend;
import com.gogidix.customersupport.supportanalytics.domain.repository.AnalyticsReportRepository;
import com.gogidix.customersupport.supportanalytics.domain.repository.ChannelPerformanceRepository;
import com.gogidix.customersupport.supportanalytics.domain.repository.TicketTrendRepository;
import com.gogidix.customersupport.supportanalytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportAnalyticsService {

    private final AnalyticsReportRepository analyticsReportRepository;
    private final TicketTrendRepository ticketTrendRepository;
    private final ChannelPerformanceRepository channelPerformanceRepository;
    private final AnalyticsReportMapper analyticsReportMapper;
    private final TicketTrendMapper ticketTrendMapper;
    private final ChannelPerformanceMapper channelPerformanceMapper;

    public List<AnalyticsReportResponseDto> getAllReports() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all analytics reports for tenant: {}", tenantId);
        return analyticsReportRepository.findByTenantIdOrderByGeneratedAtDesc(tenantId).stream()
                .map(analyticsReportMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public AnalyticsReportResponseDto getReportById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching analytics report with id: {} for tenant: {}", id, tenantId);
        AnalyticsReport report = analyticsReportRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Analytics report not found with id: " + id));
        return analyticsReportMapper.toResponseDto(report);
    }

    @Transactional
    public AnalyticsReportResponseDto createReport(AnalyticsReportRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new analytics report for tenant: {}", tenantId);

        AnalyticsReport report = analyticsReportMapper.toEntity(request, tenantId);
        AnalyticsReport saved = analyticsReportRepository.save(report);
        log.info("Created analytics report with id: {} for tenant: {}", saved.getId(), tenantId);
        return analyticsReportMapper.toResponseDto(saved);
    }

    @Transactional
    public AnalyticsReportResponseDto updateReport(String id, AnalyticsReportRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating analytics report with id: {} for tenant: {}", id, tenantId);

        AnalyticsReport existing = analyticsReportRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Analytics report not found with id: " + id));

        analyticsReportMapper.updateEntityFromDto(request, existing);
        AnalyticsReport updated = analyticsReportRepository.save(existing);
        log.info("Updated analytics report with id: {} for tenant: {}", id, tenantId);
        return analyticsReportMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteReport(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting analytics report with id: {} for tenant: {}", id, tenantId);

        if (!analyticsReportRepository.existsById(id)) {
            throw new IllegalArgumentException("Analytics report not found with id: " + id);
        }
        analyticsReportRepository.deleteByTenantIdAndId(tenantId, id);
        log.info("Deleted analytics report with id: {} for tenant: {}", id, tenantId);
    }

    public List<AnalyticsReportResponseDto> getReportsByType(AnalyticsReport.ReportType reportType) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching analytics reports by type: {} for tenant: {}", reportType, tenantId);
        return analyticsReportRepository.findByTenantIdAndReportType(tenantId, reportType).stream()
                .map(analyticsReportMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AnalyticsReportResponseDto> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching analytics reports by date range: {} to {} for tenant: {}", startDate, endDate, tenantId);
        return analyticsReportRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate).stream()
                .map(analyticsReportMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public AnalyticsReportResponseDto getLatestReport() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest analytics report for tenant: {}", tenantId);
        return analyticsReportRepository.findLatestReportByTenantId(tenantId)
                .map(analyticsReportMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("No analytics report found for tenant: " + tenantId));
    }

    public List<TicketTrendResponseDto> getTicketTrends(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching ticket trends from {} to {} for tenant: {}", startDate, endDate, tenantId);
        return ticketTrendRepository.findByTenantIdAndTrendDateBetween(tenantId, startDate, endDate).stream()
                .map(ticketTrendMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<TicketTrendResponseDto> getTicketTrendsByType(TicketTrend.PeriodType periodType) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching ticket trends by period type: {} for tenant: {}", periodType, tenantId);
        return ticketTrendRepository.findByTenantIdAndPeriodType(tenantId, periodType).stream()
                .map(ticketTrendMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public TicketTrendResponseDto getLatestTicketTrend() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest ticket trend for tenant: {}", tenantId);
        TicketTrend trend = ticketTrendRepository.findFirstByTenantIdOrderByTrendDateDesc(tenantId);
        if (trend == null) {
            throw new IllegalArgumentException("No ticket trend found for tenant: " + tenantId);
        }
        return ticketTrendMapper.toResponseDto(trend);
    }

    public List<ChannelPerformanceResponseDto> getChannelPerformance(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching channel performance from {} to {} for tenant: {}", startDate, endDate, tenantId);
        return channelPerformanceRepository.findByTenantIdAndMetricDateBetween(tenantId, startDate, endDate).stream()
                .map(channelPerformanceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ChannelPerformanceResponseDto> getChannelPerformanceByType(ChannelPerformance.ChannelType channelType,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching channel performance for type: {} from {} to {} for tenant: {}",
                channelType, startDate, endDate, tenantId);
        return channelPerformanceRepository.findByTenantIdAndChannelTypeAndMetricDateBetween(
                        tenantId, channelType, startDate, endDate).stream()
                .map(channelPerformanceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ChannelPerformanceResponseDto> getAllChannelPerformance() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all channel performance for tenant: {}", tenantId);
        return channelPerformanceRepository.findByTenantIdOrderByMetricDateDesc(tenantId).stream()
                .map(channelPerformanceMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
