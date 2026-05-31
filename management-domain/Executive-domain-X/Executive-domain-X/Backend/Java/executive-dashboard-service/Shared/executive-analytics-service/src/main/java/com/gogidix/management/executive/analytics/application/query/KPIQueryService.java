package com.gogidix.management.executive.analytics.application.query;

import com.gogidix.management.executive.analytics.application.dto.KPIDashboardDTO;
import com.gogidix.management.executive.analytics.application.dto.KPIDetailDTO;
import com.gogidix.management.executive.analytics.domain.model.KPI;
import com.gogidix.management.executive.analytics.domain.repository.KPIRepository;
import com.gogidix.management.shared.exception.NotFoundException;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KPIQueryService {
    private final KPIRepository kpiRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "kpi", key = "#id")
    public KPI getKPIById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching KPI: {} for tenant: {}", id, tenantId);
        return kpiRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("KPI", id));
    }

    @Transactional(readOnly = true)
    public KPIDetailDTO getKPIDetailById(String id) {
        KPI kpi = getKPIById(id);
        return toDetailDTO(kpi);
    }

    @Transactional(readOnly = true)
    public List<KPI> getAllKPIs() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all KPIs for tenant: {}", tenantId);
        return kpiRepository.findAllByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByCategory(String category) {
        log.debug("Fetching KPIs by category: {}", category);
        return kpiRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByExecutiveLevel(String executiveLevel) {
        log.debug("Fetching KPIs for executive level: {}", executiveLevel);
        return kpiRepository.findByExecutiveLevel(executiveLevel);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "kpiDashboard", key = "#executiveLevel")
    public List<KPI> getDashboardKPIs(String executiveLevel) {
        log.debug("Fetching dashboard KPIs for executive: {}", executiveLevel);
        return kpiRepository.findVisibleForDashboard(executiveLevel);
    }

    @Transactional(readOnly = true)
    public KPIDashboardDTO getKPIDashboard(String executiveLevel) {
        List<KPI> kpis = getDashboardKPIs(executiveLevel);
        KPIDashboardDTO dashboard = new KPIDashboardDTO();
        dashboard.setExecutiveLevel(executiveLevel);
        dashboard.setTotalKPIs(kpis.size());
        dashboard.setOnTrack((int) kpis.stream().filter(KPI::isOnTrack).count());
        dashboard.setNeedsAttention((int) kpis.stream().filter(KPI::needsAttention).count());
        List<KPIDashboardDTO.KPIDashboardItemDTO> items = kpis.stream()
            .map(kpi -> {
                KPIDashboardDTO.KPIDashboardItemDTO item = new KPIDashboardDTO.KPIDashboardItemDTO();
                item.setId(kpi.getId());
                item.setName(kpi.getName());
                item.setValue(kpi.getValue());
                item.setUnit(kpi.getUnit());
                item.setPercentChange(kpi.getPercentChange());
                item.setStatus(kpi.getStatus());
                item.setTrend(kpi.getTrend());
                item.setVisible(kpi.getVisible());
                return item;
            })
            .collect(Collectors.toList());
        dashboard.setKpis(items);
        return dashboard;
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByPeriod(String period) {
        log.debug("Fetching KPIs for period: {}", period);
        return kpiRepository.findByPeriod(period);
    }

    @Transactional(readOnly = true)
    public Optional<KPI> getLatestKPIByName(String name) {
        log.debug("Fetching latest KPI by name: {}", name);
        return kpiRepository.findLatestByName(name);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsNeedingAttention() {
        log.debug("Fetching KPIs needing attention");
        return kpiRepository.findNeedingAttention();
    }

    @Transactional(readOnly = true)
    public List<KPI> getCalculatedKPIs() {
        log.debug("Fetching calculated KPIs");
        return kpiRepository.findByIsCalculatedTrue();
    }

    @Transactional(readOnly = true)
    public List<KPI> getManualKPIs() {
        log.debug("Fetching manual KPIs");
        return kpiRepository.findByIsCalculatedFalse();
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByStatus(String status) {
        log.debug("Fetching KPIs by status: {}", status);
        return kpiRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByDataSource(String dataSource) {
        log.debug("Fetching KPIs by data source: {}", dataSource);
        return kpiRepository.findByDataSource(dataSource);
    }

    @Transactional(readOnly = true)
    public List<KPI> searchKPIs(String searchTerm) {
        log.debug("Searching KPIs with term: {}", searchTerm);
        return kpiRepository.search(searchTerm);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsWithSignificantChange(BigDecimal threshold) {
        log.debug("Fetching KPIs with significant change > {}%", threshold);
        return kpiRepository.findWithSignificantChange(threshold);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsByPeriodRange(Instant startDate, Instant endDate) {
        log.debug("Fetching KPIs between {} and {}", startDate, endDate);
        return kpiRepository.findByPeriodRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueCategories() {
        return kpiRepository.findDistinctCategories();
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueExecutiveLevels() {
        return kpiRepository.findDistinctExecutiveLevels();
    }

    @Transactional(readOnly = true)
    public List<String> getUniquePeriods() {
        return kpiRepository.findDistinctPeriods();
    }

    @Transactional(readOnly = true)
    public Page<KPI> getKPIsPaginated(String category, Pageable pageable) {
        if (category != null && !category.isBlank()) {
            return kpiRepository.findByCategory(category, pageable);
        }
        return kpiRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<KPI> getKPIsNeedingRecalculation(Instant threshold) {
        log.debug("Fetching KPIs needing recalculation since {}", threshold);
        return kpiRepository.findNeedingRecalculation(threshold);
    }

    @Transactional(readOnly = true)
    public long countByCategory(String category) {
        return kpiRepository.countByCategory(category);
    }

    @Transactional(readOnly = true)
    public boolean exists(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return kpiRepository.existsByIdAndTenantId(id, tenantId);
    }

    private KPIDetailDTO toDetailDTO(KPI kpi) {
        KPIDetailDTO dto = new KPIDetailDTO();
        dto.setId(kpi.getId());
        dto.setName(kpi.getName());
        dto.setCategory(kpi.getCategory());
        dto.setExecutiveLevel(kpi.getExecutiveLevel());
        dto.setValue(kpi.getValue());
        dto.setUnit(kpi.getUnit());
        dto.setPeriod(kpi.getPeriod());
        dto.setTarget(kpi.getTarget());
        dto.setPreviousValue(kpi.getPreviousValue());
        dto.setPercentChange(kpi.getPercentChange());
        dto.setStatus(kpi.getStatus());
        dto.setTrend(kpi.getTrend());
        dto.setDataSources(kpi.getDataSources() != null ? kpi.getDataSources() : Collections.emptyList());
        dto.setMetadata(kpi.getMetadata());
        dto.setVisible(kpi.getVisible());
        dto.setLastCalculatedAt(kpi.getLastCalculatedAt());
        dto.setCreatedAt(kpi.getCreatedAt());
        dto.setUpdatedAt(kpi.getUpdatedAt());
        return dto;
    }
}
