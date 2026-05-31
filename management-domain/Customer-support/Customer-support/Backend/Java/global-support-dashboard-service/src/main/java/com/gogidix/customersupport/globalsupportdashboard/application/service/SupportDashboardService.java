package com.gogidix.customersupport.globalsupportdashboard.application.service;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.AgentPerformanceDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.DashboardSummaryDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.RegionalMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.SupportMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.application.mapper.SupportMetricsMapper;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.AgentPerformance;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.RegionalMetrics;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.SupportMetrics;
import com.gogidix.customersupport.globalsupportdashboard.domain.repository.AgentPerformanceRepository;
import com.gogidix.customersupport.globalsupportdashboard.domain.repository.RegionalMetricsRepository;
import com.gogidix.customersupport.globalsupportdashboard.domain.repository.SupportMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Global Support Dashboard operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupportDashboardService {

    private final SupportMetricsRepository metricsRepository;
    private final RegionalMetricsRepository regionalRepository;
    private final AgentPerformanceRepository agentRepository;
    private final SupportMetricsMapper mapper;

    private static final String DEFAULT_TENANT_ID = "global";
    private static final List<String> ACTIVE_STATUSES = List.of("ACTIVE", "IN_CALL", "IN_CHAT");

    /**
     * Get global support metrics summary
     */
    @Cacheable(value = "supportMetrics", key = "#tenantId ?: 'default'")
    public SupportMetricsDto getGlobalMetrics(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        log.debug("Fetching global metrics for tenant: {}", tenantId);

        SupportMetrics metrics = metricsRepository
                .findFirstByTenantIdOrderByUpdatedAtDesc(tenantId)
                .orElse(createDefaultMetrics(tenantId));

        SupportMetricsDto dto = mapper.toDto(metrics);

        log.info("Retrieved global metrics: {} total tickets, {}% SLA compliance",
                dto.getTotalTickets(), dto.getSlaCompliancePercentage());

        return dto;
    }

    /**
     * Get complete dashboard summary
     */
    @Cacheable(value = "dashboardSummary", key = "#tenantId ?: 'default'")
    public DashboardSummaryDto getDashboardSummary(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        log.debug("Fetching dashboard summary for tenant: {}", tenantId);

        // Get global metrics
        SupportMetrics globalMetrics = metricsRepository
                .findFirstByTenantIdOrderByUpdatedAtDesc(tenantId)
                .orElse(createDefaultMetrics(tenantId));

        // Get regional metrics
        List<RegionalMetricsDto> regionalDtos = regionalRepository
                .findByTenantIdOrderByRegionNameAsc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        // Get top performers
        List<AgentPerformanceDto> topAgents = agentRepository
                .findByTenantIdOrderByTicketsResolvedDesc(tenantId)
                .stream()
                .limit(10)
                .map(mapper::toDto)
                .collect(Collectors.toList());

        // Get agents needing attention
        List<AgentPerformanceDto> agentsNeedingAttention = agentRepository
                .findAgentsNeedingAttention(tenantId, 3.5, 70.0)
                .stream()
                .limit(10)
                .map(mapper::toDto)
                .collect(Collectors.toList());

        DashboardSummaryDto summary = DashboardSummaryDto.builder()
                .tenantId(tenantId)
                .globalOverview(buildGlobalOverview(globalMetrics))
                .regionalMetrics(regionalDtos)
                .topAgents(topAgents)
                .agentsNeedingAttention(agentsNeedingAttention)
                .trends(buildTrends(globalMetrics))
                .generatedAt(Instant.now())
                .isStale(globalMetrics.isStale())
                .build();

        log.info("Generated dashboard summary with {} regions, {} top agents",
                regionalDtos.size(), topAgents.size());

        return summary;
    }

    /**
     * Get metrics by region
     */
    @Cacheable(value = "regionalMetrics", key = "#tenantId + ':' + #regionCode")
    public RegionalMetricsDto getRegionalMetrics(String tenantId, String regionCode) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        log.debug("Fetching regional metrics for tenant: {}, region: {}", tenantId, regionCode);

        return regionalRepository
                .findByTenantIdAndRegionCode(tenantId, regionCode)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No metrics found for region: " + regionCode));
    }

    /**
     * Get all regional metrics
     */
    public List<RegionalMetricsDto> getAllRegionalMetrics(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return regionalRepository
                .findByTenantIdOrderByRegionNameAsc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get agent performance
     */
    @Cacheable(value = "agentPerformance", key = "#tenantId + ':' + #agentId")
    public AgentPerformanceDto getAgentPerformance(String tenantId, String agentId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        log.debug("Fetching agent performance for tenant: {}, agent: {}", tenantId, agentId);

        return agentRepository
                .findByTenantIdAndAgentId(tenantId, agentId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No performance data found for agent: " + agentId));
    }

    /**
     * Get top performing agents
     */
    public List<AgentPerformanceDto> getTopAgents(String tenantId, int limit) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return agentRepository
                .findByTenantIdOrderByTicketsResolvedDesc(tenantId)
                .stream()
                .limit(limit)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get active agents
     */
    public List<AgentPerformanceDto> getActiveAgents(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return agentRepository
                .findByTenantIdAndStatusIn(tenantId, ACTIVE_STATUSES)
                .stream()
                .map(mapper::toDto)
                .sorted(Comparator.comparing(AgentPerformanceDto::getAgentName))
                .collect(Collectors.toList());
    }

    /**
     * Refresh metrics - triggers aggregation
     */
    @CacheEvict(value = {"supportMetrics", "dashboardSummary", "regionalMetrics", "agentPerformance"},
               allEntries = true)
    @Transactional
    public SupportMetricsDto refreshMetrics(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        log.info("Refreshing metrics for tenant: {}", tenantId);

        // In production, this would trigger an aggregation job
        // For now, we'll update the last refreshed timestamp
        SupportMetrics metrics = metricsRepository
                .findFirstByTenantIdOrderByUpdatedAtDesc(tenantId)
                .orElse(createDefaultMetrics(tenantId));

        metrics.setLastRefreshedAt(Instant.now());
        metrics.updateTimestamp();

        SupportMetrics saved = metricsRepository.save(metrics);

        log.info("Metrics refreshed for tenant: {}", tenantId);

        return mapper.toDto(saved);
    }

    /**
     * Create default metrics for testing/initialization
     */
    private SupportMetrics createDefaultMetrics(String tenantId) {
        SupportMetrics metrics = new SupportMetrics(tenantId);

        // Set default values
        metrics.setTotalTickets(0L);
        metrics.setOpenTickets(0L);
        metrics.setResolvedTickets(0L);
        metrics.setSlaCompliancePercentage(100.0);
        metrics.setAggregationType("DAILY");
        metrics.setMetricStartDate(Instant.now().minusSeconds(86400));
        metrics.setMetricEndDate(Instant.now());

        return metricsRepository.save(metrics);
    }

    /**
     * Build global overview from metrics
     */
    private DashboardSummaryDto.GlobalOverviewDto buildGlobalOverview(SupportMetrics metrics) {
        return DashboardSummaryDto.GlobalOverviewDto.builder()
                .totalTickets(metrics.getTotalTickets())
                .openTickets(metrics.getOpenTickets())
                .avgCsatScore(metrics.getAvgCsatScore())
                .slaCompliancePercentage(metrics.getSlaCompliancePercentage())
                .totalAgents(metrics.getTotalAgents())
                .activeAgents(metrics.getActiveAgents())
                .avgResolutionTimeMinutes(metrics.getAvgResolutionTimeMinutes() != null ? metrics.getAvgResolutionTimeMinutes().doubleValue() : 0.0)
                .build();
    }

    /**
     * Build trends from metrics
     */
    private DashboardSummaryDto.TrendDto buildTrends(SupportMetrics metrics) {
        return DashboardSummaryDto.TrendDto.builder()
                .ticketVolumeChange(0.0)
                .csatChange(0.0)
                .slaChange(0.0)
                .period("24h")
                .build();
    }
}
