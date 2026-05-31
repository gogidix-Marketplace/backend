package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.Quota;
import com.gogidix.sales.territory.domain.repository.QuotaRepository;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Quota Query Service
 * Handles all read operations for quotas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuotaQueryService {

    private final QuotaRepository quotaRepository;
    private final MongoTemplate mongoTemplate;

    public Quota getById(String quotaId) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByQuotaIdAndTenantId(quotaId, tenantId)
            .orElseThrow(() -> new NotFoundException("Quota", quotaId));
    }

    public List<Quota> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantId(tenantId);
    }

    public List<Quota> getByTerritoryId(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndTerritoryId(tenantId, territoryId);
    }

    public List<Quota> getActiveByTerritoryId(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findActiveByTenantIdAndTerritoryId(tenantId, territoryId);
    }

    public List<Quota> getBySalesRepresentativeId(String salesRepresentativeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndSalesRepresentativeId(tenantId, salesRepresentativeId);
    }

    public List<Quota> getActiveBySalesRepresentativeId(String salesRepresentativeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findActiveByTenantIdAndSalesRepresentativeId(tenantId, salesRepresentativeId);
    }

    public List<Quota> getByStatus(Quota.QuotaStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Quota> getByType(Quota.QuotaType type) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndType(tenantId, type);
    }

    public List<Quota> getByPeriod(Quota.QuotaPeriod period) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    public List<Quota> getByYear(Integer year) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndYear(tenantId, year);
    }

    public List<Quota> getByYearAndMonth(Integer year, Integer month) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndYearAndMonth(tenantId, year, month);
    }

    public Quota getActiveByTerritoryIdAndType(String territoryId, Quota.QuotaType type) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findActiveByTenantIdAndTerritoryIdAndType(tenantId, territoryId, type)
            .orElseThrow(() -> new NotFoundException("Active Quota", territoryId + "-" + type));
    }

    public List<Quota> getByPeriodBetween(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return quotaRepository.findByTenantIdAndPeriodBetween(tenantId, startDate, endDate);
    }

    public QuotaSummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long totalQuotas = quotaRepository.countByTenantId(tenantId);
        List<Quota> allQuotas = quotaRepository.findByTenantId(tenantId);

        long activeQuotas = allQuotas.stream()
            .filter(q -> q.getStatus() == Quota.QuotaStatus.ACTIVE)
            .count();

        long completedQuotas = allQuotas.stream()
            .filter(q -> q.getStatus() == Quota.QuotaStatus.COMPLETED)
            .count();

        BigDecimal totalAmount = allQuotas.stream()
            .map(Quota::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAchieved = allQuotas.stream()
            .map(Quota::getCurrentAchievement)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Quota.QuotaType, Long> countByType = allQuotas.stream()
            .collect(Collectors.groupingBy(Quota::getType, Collectors.counting()));

        Map<Quota.QuotaStatus, Long> countByStatus = allQuotas.stream()
            .collect(Collectors.groupingBy(Quota::getStatus, Collectors.counting()));

        return QuotaSummary.builder()
            .totalQuotas(totalQuotas)
            .activeQuotas(activeQuotas)
            .completedQuotas(completedQuotas)
            .totalAmount(totalAmount)
            .totalAchieved(totalAchieved)
            .countByType(countByType)
            .countByStatus(countByStatus)
            .build();
    }

    public Page<Quota> searchQuotas(String territoryId, String salesRepresentativeId,
                                     Quota.QuotaType type, Quota.QuotaStatus status,
                                     Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();

        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        if (territoryId != null && !territoryId.isEmpty()) {
            criteria = criteria.and("territoryId").is(territoryId);
        }

        if (salesRepresentativeId != null && !salesRepresentativeId.isEmpty()) {
            criteria = criteria.and("salesRepresentativeId").is(salesRepresentativeId);
        }

        if (type != null) {
            criteria = criteria.and("type").is(type);
        }

        if (status != null) {
            criteria = criteria.and("status").is(status);
        }

        Query query = Query.query(criteria).with(pageable);
        List<Quota> results = mongoTemplate.find(query, Quota.class);

        long count = mongoTemplate.count(Query.query(criteria), Quota.class);

        return new PageImpl<>(results, pageable, count);
    }

    /**
     * Quota Summary DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class QuotaSummary {
        private Long totalQuotas;
        private Long activeQuotas;
        private Long completedQuotas;
        private BigDecimal totalAmount;
        private BigDecimal totalAchieved;
        private Map<Quota.QuotaType, Long> countByType;
        private Map<Quota.QuotaStatus, Long> countByStatus;
    }
}
