package com.gogidix.management.executive.analytics.application.command;

import com.gogidix.management.executive.analytics.application.dto.CreateKPIRequest;
import com.gogidix.management.executive.analytics.application.dto.UpdateKPIRequest;
import com.gogidix.management.executive.analytics.domain.model.KPI;
import com.gogidix.management.executive.analytics.domain.repository.KPIRepository;
import com.gogidix.management.shared.exception.ConflictException;
import com.gogidix.management.shared.exception.NotFoundException;
import com.gogidix.management.shared.exception.ValidationException;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KPICommandService {
    private final KPIRepository kpiRepository;

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "kpis", allEntries = true),
        @CacheEvict(value = "kpiDashboard", allEntries = true)
    })
    public KPI createKPI(CreateKPIRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Creating KPI for tenant: {}, name: {}", tenantId, request.getName());
        validateCreateRequest(request);
        Optional<KPI> existingOpt = kpiRepository.findByTenantIdAndCategoryAndPeriod(
            tenantId, request.getCategory(), request.getPeriod()
        );
        List<KPI> existing = existingOpt.map(List::of).orElse(List.of());
        boolean duplicateName = existing.stream()
            .anyMatch(k -> k.getName().equalsIgnoreCase(request.getName()));
        if (duplicateName) {
            throw new ConflictException("KPI", "KPI with name '" + request.getName() + "' already exists for period " + request.getPeriod());
        }
        KPI kpi = new KPI(tenantId, request.getName(), request.getCategory(),
            request.getExecutiveLevel(), request.getValue(), request.getPeriod());
        kpi.setUnit(request.getUnit());
        kpi.setTarget(request.getTarget());
        kpi.setPreviousValue(request.getPreviousValue());
        kpi.setDataSources(request.getDataSources());
        kpi.setMetadata(request.getMetadata());
        kpi.setVisible(request.getVisible() != null ? request.getVisible() : true);
        kpi.setIsCalculated(request.getIsCalculated() != null ? request.getIsCalculated() : true);
        kpi.calculatePercentChange();
        kpi.updateStatus();
        KPI saved = kpiRepository.save(kpi);
        log.info("KPI created successfully: {}", saved.getId());
        return saved;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "kpis", allEntries = true),
        @CacheEvict(value = "kpiDashboard", allEntries = true)
    })
    public KPI updateKPI(String id, UpdateKPIRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Updating KPI: {} for tenant: {}", id, tenantId);
        KPI kpi = kpiRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("KPI", id));
        if (request.getName() != null) {
            kpi.setName(request.getName());
        }
        if (request.getCategory() != null) {
            kpi.setCategory(request.getCategory());
        }
        if (request.getExecutiveLevel() != null) {
            kpi.setExecutiveLevel(request.getExecutiveLevel());
        }
        if (request.getValue() != null) {
            kpi.setValue(request.getValue());
        }
        if (request.getUnit() != null) {
            kpi.setUnit(request.getUnit());
        }
        if (request.getPeriod() != null) {
            kpi.setPeriod(request.getPeriod());
        }
        if (request.getTarget() != null) {
            kpi.setTarget(request.getTarget());
        }
        if (request.getPreviousValue() != null) {
            kpi.setPreviousValue(request.getPreviousValue());
        }
        if (request.getDataSources() != null) {
            kpi.setDataSources(request.getDataSources());
        }
        if (request.getMetadata() != null) {
            kpi.setMetadata(request.getMetadata());
        }
        if (request.getVisible() != null) {
            kpi.setVisible(request.getVisible());
        }
        kpi.markAsRecalculated();
        KPI updated = kpiRepository.save(kpi);
        log.info("KPI updated successfully: {}", updated.getId());
        return updated;
    }

    @Transactional
    @CacheEvict(value = {"kpi", "kpis", "kpiDashboard"}, allEntries = true)
    public void deleteKPI(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Deleting KPI: {} for tenant: {}", id, tenantId);
        if (!kpiRepository.existsByIdAndTenantId(id, tenantId)) {
            throw new NotFoundException("KPI", id);
        }
        kpiRepository.deleteByIdAndTenantId(id, tenantId);
        log.info("KPI deleted successfully: {}", id);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "kpis", allEntries = true),
        @CacheEvict(value = "kpiDashboard", allEntries = true)
    })
    public List<KPI> bulkCreateKPIs(List<CreateKPIRequest> requests) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Bulk creating {} KPIs for tenant: {}", requests.size(), tenantId);
        List<KPI> created = requests.stream()
            .map(this::createKPI)
            .toList();
        log.info("Bulk KPI creation complete: {} KPIs created", created.size());
        return created;
    }

    @Transactional
    @CacheEvict(value = "kpi", key = "#id")
    public void markForRecalculation(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Marking KPI for recalculation: {} for tenant: {}", id, tenantId);
        KPI kpi = kpiRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("KPI", id));
        kpi.setLastCalculatedAt(Instant.now().minusSeconds(86401));
        kpi.touch();
        kpiRepository.save(kpi);
    }

    @Transactional
    @CacheEvict(value = "kpi", key = "#id")
    public void setVisibility(String id, boolean visible) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Setting KPI visibility: {} = {} for tenant: {}", id, visible, tenantId);
        KPI kpi = kpiRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("KPI", id));
        kpi.setVisible(visible);
        kpiRepository.save(kpi);
    }

    @Transactional
    @CachePut(value = "kpi", key = "#id")
    public KPI setManualValue(String id, BigDecimal value, String updatedBy) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Setting manual value for KPI: {} = {} for tenant: {}, user: {}", id, value, tenantId, updatedBy);
        KPI kpi = kpiRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("KPI", id));
        kpi.setValue(value);
        kpi.setIsCalculated(false);
        kpi.touch();
        return kpiRepository.save(kpi);
    }

    private void validateCreateRequest(CreateKPIRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("name", "KPI name is required");
        }
        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new ValidationException("category", "Category is required");
        }
        if (request.getValue() == null) {
            throw new ValidationException("value", "Value is required");
        }
        if (request.getPeriod() == null || request.getPeriod().isBlank()) {
            request.setPeriod(DateTimeFormatter.ofPattern("yyyy-MM").format(Instant.now().atZone(ZoneId.systemDefault())));
        }
        if (request.getExecutiveLevel() != null) {
            String level = request.getExecutiveLevel().toUpperCase();
            if (!level.matches("(CEO|COO|CFO|CTO|ALL)")) {
                throw new ValidationException("executiveLevel", "Must be one of: CEO, COO, CFO, CTO, ALL");
            }
            request.setExecutiveLevel(level);
        }
        if (request.getCategory() != null) {
            String category = request.getCategory().toUpperCase();
            if (!category.matches("(FINANCIAL|OPERATIONAL|CUSTOMER|EMPLOYEE|STRATEGIC|TECHNOLOGY)")) {
                throw new ValidationException("category", "Must be one of: FINANCIAL, OPERATIONAL, CUSTOMER, EMPLOYEE, STRATEGIC, TECHNOLOGY");
            }
            request.setCategory(category);
        }
    }
}
