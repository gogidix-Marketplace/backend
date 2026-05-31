package com.gogidix.globalbusinessmanagement.dashboard.application.service;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto;
import com.gogidix.globalbusinessmanagement.dashboard.application.mapper.GlobalBusinessMetricsMapper;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.GlobalBusinessMetrics;
import com.gogidix.globalbusinessmanagement.dashboard.domain.repository.GlobalBusinessMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing GlobalBusinessMetrics entities.
 * Provides business logic for aggregating and calculating global business metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlobalBusinessMetricsService {

    private final GlobalBusinessMetricsRepository repository;
    private final GlobalBusinessMetricsMapper mapper;

    private static final String CACHE_NAME = "globalMetrics";

    /**
     * Create a new GlobalBusinessMetrics entity.
     *
     * @param dto the metrics DTO
     * @return the created metrics DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public GlobalBusinessMetricsDto createMetrics(GlobalBusinessMetricsDto dto) {
        log.info("Creating new global business metrics for period: {}", dto.getPeriodId());

        GlobalBusinessMetrics entity = mapper.toEntity(dto);

        // Calculate derived metrics
        calculateDerivedMetrics(entity);

        // Set default status if not provided
        if (entity.getStatus() == null) {
            entity.setStatus(GlobalBusinessMetrics.MetricsStatus.DRAFT);
        }

        entity = repository.save(entity);
        log.info("Created global business metrics with ID: {}", entity.getId());

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Update an existing GlobalBusinessMetrics entity.
     *
     * @param id  the metrics ID
     * @param dto the metrics DTO with updated values
     * @return the updated metrics DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public GlobalBusinessMetricsDto updateMetrics(String id, GlobalBusinessMetricsDto dto) {
        log.info("Updating global business metrics with ID: {}", id);

        GlobalBusinessMetrics entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GlobalBusinessMetrics not found with id: " + id));

        mapper.updateEntityFromDto(dto, entity);

        // Recalculate derived metrics
        calculateDerivedMetrics(entity);

        // Increment version
        entity.setVersion(entity.getVersion() != null ? entity.getVersion() + 1 : 1);

        entity = repository.save(entity);
        log.info("Updated global business metrics with ID: {}", id);

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get GlobalBusinessMetrics by ID.
     *
     * @param id the metrics ID
     * @return the metrics DTO
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public GlobalBusinessMetricsDto getMetricsById(String id) {
        log.debug("Fetching global business metrics by ID: {}", id);

        GlobalBusinessMetrics entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GlobalBusinessMetrics not found with id: " + id));

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get GlobalBusinessMetrics by period ID.
     *
     * @param periodId the period identifier
     * @return the metrics DTO
     */
    @Cacheable(value = CACHE_NAME, key = "'period:' + #periodId")
    public GlobalBusinessMetricsDto getMetricsByPeriod(String periodId) {
        log.debug("Fetching global business metrics by period: {}", periodId);

        GlobalBusinessMetrics entity = repository.findByPeriodId(periodId)
            .orElseThrow(() -> new IllegalArgumentException(
                "GlobalBusinessMetrics not found for period: " + periodId));

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get the most recent published GlobalBusinessMetrics.
     *
     * @return the most recent metrics DTO
     */
    @Cacheable(value = CACHE_NAME, key = "'latest'")
    public GlobalBusinessMetricsDto getLatestMetrics() {
        log.debug("Fetching latest published global business metrics");

        List<GlobalBusinessMetrics> metrics = repository.findMostRecentByStatusOrderByEndDateDesc(
            org.springframework.data.domain.PageRequest.of(0, 1));

        if (metrics.isEmpty()) {
            throw new IllegalArgumentException("No published global business metrics found");
        }

        return enrichWithCalculatedMetrics(mapper.toDto(metrics.get(0)));
    }

    /**
     * Get all GlobalBusinessMetrics with pagination.
     *
     * @param pageable the pagination information
     * @return page of metrics DTOs
     */
    public Page<GlobalBusinessMetricsDto> getAllMetrics(Pageable pageable) {
        log.debug("Fetching all global business metrics with pagination");

        Page<GlobalBusinessMetrics> entities = repository.findAllByOrderByEndDateDesc(pageable);
        List<GlobalBusinessMetricsDto> dtos = entities.stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }

    /**
     * Get GlobalBusinessMetrics by date range.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of metrics DTOs
     */
    public List<GlobalBusinessMetricsDto> getMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching global business metrics for date range: {} to {}", startDate, endDate);

        return repository.findByStartDateBetween(startDate, endDate).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get GlobalBusinessMetrics by status.
     *
     * @param status the metrics status
     * @return list of metrics DTOs
     */
    public List<GlobalBusinessMetricsDto> getMetricsByStatus(String status) {
        log.debug("Fetching global business metrics by status: {}", status);

        GlobalBusinessMetrics.MetricsStatus metricsStatus =
            GlobalBusinessMetrics.MetricsStatus.valueOf(status.toUpperCase());

        return repository.findByStatus(metricsStatus).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Delete GlobalBusinessMetrics by ID.
     *
     * @param id the metrics ID
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteMetrics(String id) {
        log.info("Deleting global business metrics with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("GlobalBusinessMetrics not found with id: " + id);
        }

        repository.deleteById(id);
        log.info("Deleted global business metrics with ID: {}", id);
    }

    /**
     * Aggregate metrics from multiple regions.
     *
     * @param regionalMetrics list of regional metrics
     * @param periodId        the target period ID
     * @return aggregated global metrics DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public GlobalBusinessMetricsDto aggregateRegionalMetrics(
        List<com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary> regionalMetrics,
        String periodId) {

        log.info("Aggregating metrics from {} regions for period: {}", regionalMetrics.size(), periodId);

        GlobalBusinessMetrics aggregated = GlobalBusinessMetrics.builder()
            .periodId(periodId)
            .startDate(regionalMetrics.stream()
                .min(Comparator.comparing(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getStartDate))
                .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getStartDate)
                .orElse(LocalDateTime.now()))
            .endDate(regionalMetrics.stream()
                .max(Comparator.comparing(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getEndDate))
                .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getEndDate)
                .orElse(LocalDateTime.now()))
            .baseCurrency("USD")
            .status(GlobalBusinessMetrics.MetricsStatus.PUBLISHED)
            .build();

        // Aggregate values from all regions
        BigDecimal totalRevenue = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getRevenue)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getExpenses)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalOrders = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getOrderCount)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long totalCustomers = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getCustomerCount)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long newCustomers = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getNewCustomers)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long churnedCustomers = regionalMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getChurnedCustomers)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        aggregated.setTotalRevenue(totalRevenue);
        aggregated.setTotalExpenses(totalExpenses);
        aggregated.setTotalOrders(totalOrders);
        aggregated.setActiveCustomers(totalCustomers);
        aggregated.setNewCustomers(newCustomers);
        aggregated.setChurnedCustomers(churnedCustomers);

        // Build regional breakdown
        Map<String, GlobalBusinessMetrics.RegionalContribution> regionalBreakdown = regionalMetrics.stream()
            .collect(Collectors.toMap(
                com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary::getRegionCode,
                region -> GlobalBusinessMetrics.RegionalContribution.builder()
                    .regionCode(region.getRegionCode())
                    .regionName(region.getRegionName())
                    .revenue(region.getRevenue())
                    .revenueContribution(totalRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                        region.getRevenue().divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                        BigDecimal.ZERO)
                    .growthRate(region.getGrowthRate())
                    .orders(region.getOrderCount())
                    .customers(region.getCustomerCount())
                    .profitMargin(region.getProfitMargin())
                    .build()
            ));

        aggregated.setRegionalBreakdown(regionalBreakdown);

        // Calculate derived metrics
        calculateDerivedMetrics(aggregated);

        aggregated = repository.save(aggregated);
        log.info("Created aggregated global metrics with ID: {}", aggregated.getId());

        return enrichWithCalculatedMetrics(mapper.toDto(aggregated));
    }

    /**
     * Get top performing regions by revenue.
     *
     * @param periodId the period identifier
     * @param limit    the maximum number of regions to return
     * @return list of regional contribution DTOs
     */
    @Cacheable(value = CACHE_NAME, key = "'topRegions:' + #periodId + ':' + #limit")
    public List<GlobalBusinessMetricsDto.RegionalContributionDto> getTopPerformingRegions(
        String periodId, int limit) {

        log.debug("Fetching top {} performing regions for period: {}", limit, periodId);

        GlobalBusinessMetrics metrics = repository.findByPeriodId(periodId)
            .orElseThrow(() -> new IllegalArgumentException(
                "GlobalBusinessMetrics not found for period: " + periodId));

        return metrics.getRegionalBreakdown().values().stream()
            .sorted(Comparator.comparing(
                GlobalBusinessMetrics.RegionalContribution::getRevenue, Comparator.reverseOrder()))
            .limit(limit)
            .map(mapper::toRegionalContributionDto)
            .toList();
    }

    /**
     * Get financial summary for a period.
     *
     * @param periodId the period identifier
     * @return financial summary
     */
    @Cacheable(value = CACHE_NAME, key = "'financialSummary:' + #periodId")
    public Map<String, Object> getFinancialSummary(String periodId) {
        log.debug("Fetching financial summary for period: {}", periodId);

        GlobalBusinessMetrics metrics = repository.findByPeriodId(periodId)
            .orElseThrow(() -> new IllegalArgumentException(
                "GlobalBusinessMetrics not found for period: " + periodId));

        return Map.of(
            "totalRevenue", metrics.getTotalRevenue(),
            "totalExpenses", metrics.getTotalExpenses(),
            "grossProfit", metrics.getGrossProfit(),
            "netProfit", metrics.getNetProfit(),
            "profitMargin", metrics.getProfitMargin(),
            "averageOrderValue", metrics.getAverageOrderValue(),
            "revenuePerCustomer", metrics.calculateRevenuePerCustomer(),
            "currency", metrics.getBaseCurrency()
        );
    }

    /**
     * Publish metrics (change status to PUBLISHED).
     *
     * @param id the metrics ID
     * @return the published metrics DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public GlobalBusinessMetricsDto publishMetrics(String id) {
        log.info("Publishing global business metrics with ID: {}", id);

        GlobalBusinessMetrics entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GlobalBusinessMetrics not found with id: " + id));

        entity.setStatus(GlobalBusinessMetrics.MetricsStatus.PUBLISHED);
        entity = repository.save(entity);

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Calculate derived metrics for a GlobalBusinessMetrics entity.
     *
     * @param metrics the metrics entity
     */
    private void calculateDerivedMetrics(GlobalBusinessMetrics metrics) {
        // Calculate gross profit
        if (metrics.getTotalRevenue() != null && metrics.getTotalExpenses() != null) {
            metrics.setGrossProfit(metrics.getTotalRevenue().subtract(metrics.getTotalExpenses()));
        }

        // Calculate net profit (same as gross profit for now, can be enhanced with tax calculations)
        metrics.setNetProfit(metrics.getGrossProfit());

        // Calculate profit margin
        if (metrics.getTotalRevenue() != null && metrics.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0) {
            if (metrics.getGrossProfit() != null) {
                metrics.setProfitMargin(
                    metrics.getGrossProfit().divide(metrics.getTotalRevenue(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            }
        }

        // Calculate average order value
        if (metrics.getTotalOrders() != null && metrics.getTotalOrders() > 0 && metrics.getTotalRevenue() != null) {
            metrics.setAverageOrderValue(
                metrics.getTotalRevenue().divide(BigDecimal.valueOf(metrics.getTotalOrders()), 2, RoundingMode.HALF_UP));
        }

        // Calculate customer retention rate
        if (metrics.getActiveCustomers() != null && metrics.getActiveCustomers() > 0) {
            long retainedCustomers = metrics.getActiveCustomers() - (metrics.getNewCustomers() != null ? metrics.getNewCustomers() : 0);
            metrics.setCustomerRetentionRate(
                BigDecimal.valueOf((double) retainedCustomers / metrics.getActiveCustomers() * 100)
                    .setScale(2, RoundingMode.HALF_UP));
        }

        // Set default currency if not provided
        if (metrics.getBaseCurrency() == null) {
            metrics.setBaseCurrency("USD");
        }
    }

    /**
     * Enrich DTO with calculated metrics.
     *
     * @param dto the metrics DTO
     * @return enriched metrics DTO
     */
    private GlobalBusinessMetricsDto enrichWithCalculatedMetrics(GlobalBusinessMetricsDto dto) {
        if (dto == null) {
            return null;
        }

        GlobalBusinessMetricsDto.CalculatedMetrics calculated = GlobalBusinessMetricsDto.CalculatedMetrics.builder()
            .customerGrowthRate(BigDecimal.ZERO)
            .customerChurnRate(BigDecimal.ZERO)
            .revenuePerCustomer(BigDecimal.ZERO)
            .ordersPerCustomer(BigDecimal.ZERO)
            .build();

        if (dto.getActiveCustomers() != null && dto.getActiveCustomers() > 0) {
            if (dto.getNewCustomers() != null) {
                calculated.setCustomerGrowthRate(
                    BigDecimal.valueOf(dto.getNewCustomers()).divide(BigDecimal.valueOf(dto.getActiveCustomers()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            }

            if (dto.getChurnedCustomers() != null) {
                calculated.setCustomerChurnRate(
                    BigDecimal.valueOf(dto.getChurnedCustomers()).divide(BigDecimal.valueOf(dto.getActiveCustomers()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            }

            if (dto.getTotalRevenue() != null) {
                calculated.setRevenuePerCustomer(
                    dto.getTotalRevenue().divide(BigDecimal.valueOf(dto.getActiveCustomers()), 2, RoundingMode.HALF_UP));
            }

            if (dto.getTotalOrders() != null) {
                calculated.setOrdersPerCustomer(
                    BigDecimal.valueOf(dto.getTotalOrders()).divide(BigDecimal.valueOf(dto.getActiveCustomers()), 2, RoundingMode.HALF_UP));
            }

            BigDecimal netGrowth = calculated.getCustomerGrowthRate().subtract(calculated.getCustomerChurnRate());
            calculated.setNetCustomerGrowthRate(netGrowth);
        }

        dto.setCalculatedMetrics(calculated);
        return dto;
    }

    /**
     * Check if metrics exist for a period.
     *
     * @param periodId the period identifier
     * @return true if metrics exist, false otherwise
     */
    public boolean existsByPeriod(String periodId) {
        return repository.existsByPeriodId(periodId);
    }

    /**
     * Get metrics count by status.
     *
     * @param status the metrics status
     * @return count of metrics
     */
    public long countByStatus(String status) {
        return repository.countByStatus(GlobalBusinessMetrics.MetricsStatus.valueOf(status.toUpperCase()));
    }
}
