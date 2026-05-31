package com.gogidix.dashboard.core.application.service;

import com.gogidix.dashboard.core.application.dto.response.KPIResponseDto;
import com.gogidix.dashboard.core.application.dto.response.PagedResponseDto;
import com.gogidix.dashboard.core.application.mapper.KPIMapper;
import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import com.gogidix.dashboard.core.domain.model.KPIValue;
import com.gogidix.dashboard.core.domain.model.SourceDomain;
import com.gogidix.dashboard.core.domain.port.in.GetKPIQuery;
import com.gogidix.dashboard.core.domain.port.in.SearchKPIsQuery;
import com.gogidix.dashboard.core.domain.port.out.DashboardKPIRepository;
import com.gogidix.dashboard.core.domain.port.out.KPIValueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Query service for KPI operations.
 * Handles read operations for KPI retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KPIQueryService {

    private final DashboardKPIRepository kpiRepository;
    private final KPIValueRepository valueRepository;
    private final KPIMapper mapper;

    /**
     * Get KPI by ID
     */
    public KPIResponseDto getKPIById(String kpiId, GetKPIQuery query) {
        log.info("Getting KPI by ID: id={}", kpiId);

        DashboardKPI kpi = kpiRepository.findById(UUID.fromString(kpiId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + kpiId));

        KPIResponseDto dto = mapper.toDto(kpi);

        if (query.getIncludeHistoricalValues()) {
            LocalDateTime startDate = LocalDateTime.now().minusDays(query.getHistoricalDays());
            List<KPIValue> values = valueRepository.findByKpiIdAndRecordedAtBetween(
                    kpi.getId(), startDate, LocalDateTime.now());
            dto.setHistoricalValues(values.stream()
                    .map(mapper::toValueDto)
                    .collect(Collectors.toList()));
        }

        log.info("KPI retrieved successfully: id={}", kpiId);
        return dto;
    }

    /**
     * Get KPI by code
     */
    public KPIResponseDto getKPIByCode(String code, String tenantId) {
        log.info("Getting KPI by code: code={}, tenant={}", code, tenantId);

        DashboardKPI kpi = kpiRepository.findByCode(code)
                .filter(k -> k.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("KPI not found: " + code));

        return mapper.toDto(kpi);
    }

    /**
     * Search KPIs with filters
     */
    public PagedResponseDto<KPIResponseDto> searchKPIs(SearchKPIsQuery query) {
        log.info("Searching KPIs: tenant={}, category={}, source={}",
                query.getTenantId(), query.getCategory(), query.getSourceDomain());

        List<DashboardKPI> kpis;

        if (query.getCodes() != null && !query.getCodes().isEmpty()) {
            kpis = kpiRepository.findByTenantIdAndCodeIn(query.getTenantId(), query.getCodes());
        } else if (query.getSearchQuery() != null && !query.getSearchQuery().isEmpty()) {
            kpis = kpiRepository.searchByTenantIdAndSearchQuery(query.getTenantId(), query.getSearchQuery());
        } else if (query.getCategory() != null) {
            kpis = kpiRepository.findByTenantIdAndCategory(query.getTenantId(), query.getCategory());
        } else if (query.getSourceDomain() != null) {
            kpis = kpiRepository.findByTenantIdAndSourceDomain(
                    query.getTenantId(), SourceDomain.valueOf(query.getSourceDomain()));
        } else if (query.getIsActive() != null) {
            kpis = kpiRepository.findByTenantIdAndIsActive(query.getTenantId(), query.getIsActive());
        } else {
            kpis = kpiRepository.findByTenantId(query.getTenantId());
        }

        // Apply pagination
        int start = query.getPage() * query.getSize();
        int end = Math.min(start + query.getSize(), kpis.size());

        List<DashboardKPI> pagedKpis = kpis.subList(start, end);
        List<KPIResponseDto> dtos = mapper.toDtoList(pagedKpis);

        return PagedResponseDto.<KPIResponseDto>builder()
                .content(dtos)
                .pageNumber(query.getPage())
                .pageSize(query.getSize())
                .totalElements(kpis.size())
                .totalPages((int) Math.ceil((double) kpis.size() / query.getSize()))
                .first(query.getPage() == 0)
                .last(end >= kpis.size())
                .empty(kpis.isEmpty())
                .build();
    }

    /**
     * Get KPIs by category
     */
    public List<KPIResponseDto> getKPIsByCategory(String tenantId, String category) {
        log.info("Getting KPIs by category: tenant={}, category={}", tenantId, category);

        List<DashboardKPI> kpis = kpiRepository.findByTenantIdAndCategory(tenantId, category);
        return mapper.toDtoList(kpis);
    }

    /**
     * Get KPIs by source domain
     */
    public List<KPIResponseDto> getKPIsBySourceDomain(String tenantId, String sourceDomain) {
        log.info("Getting KPIs by source domain: tenant={}, source={}", tenantId, sourceDomain);

        List<DashboardKPI> kpis = kpiRepository.findByTenantIdAndSourceDomain(
                tenantId, SourceDomain.valueOf(sourceDomain));
        return mapper.toDtoList(kpis);
    }

    /**
     * Get real-time KPIs
     */
    public List<KPIResponseDto> getRealTimeKPIs(String tenantId) {
        log.info("Getting real-time KPIs: tenant={}", tenantId);

        List<DashboardKPI> kpis = kpiRepository.findByTenantIdAndIsRealTime(tenantId, true);
        return mapper.toDtoList(kpis);
    }

    /**
     * Count KPIs by tenant
     */
    public long countKPIs(String tenantId) {
        return kpiRepository.countByTenantId(tenantId);
    }
}
