package com.gogidix.globalbusinessmanagement.dashboard.application.service;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto;
import com.gogidix.globalbusinessmanagement.dashboard.application.mapper.RegionalSummaryMapper;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
import com.gogidix.globalbusinessmanagement.dashboard.domain.repository.RegionalSummaryRepository;
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
 * Service for managing RegionalSummary entities.
 * Provides business logic for regional business summaries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionalSummaryService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegionalSummaryService.class);

    private final RegionalSummaryRepository repository;
    private final RegionalSummaryMapper mapper;

    private static final String CACHE_NAME = "regionalSummary";

    /**
     * Create a new RegionalSummary entity.
     *
     * @param dto the regional summary DTO
     * @return the created regional summary DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public RegionalSummaryDto createSummary(RegionalSummaryDto dto) {
        log.info("Creating new regional summary for region: {}, period: {}",
            dto.getRegionCode(), dto.getPeriodId());

        RegionalSummary entity = mapper.toEntity(dto);

        // Calculate derived metrics
        calculateDerivedMetrics(entity);

        // Set default status if not provided
        if (entity.getStatus() == null) {
            entity.setStatus(RegionalSummary.SummaryStatus.ACTIVE);
        }

        entity = repository.save(entity);
        log.info("Created regional summary with ID: {}", entity.getId());

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Update an existing RegionalSummary entity.
     *
     * @param id  the summary ID
     * @param dto the regional summary DTO with updated values
     * @return the updated regional summary DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public RegionalSummaryDto updateSummary(String id, RegionalSummaryDto dto) {
        log.info("Updating regional summary with ID: {}", id);

        RegionalSummary entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("RegionalSummary not found with id: " + id));

        mapper.updateEntityFromDto(dto, entity);

        // Recalculate derived metrics
        calculateDerivedMetrics(entity);

        entity = repository.save(entity);
        log.info("Updated regional summary with ID: {}", id);

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get RegionalSummary by ID.
     *
     * @param id the summary ID
     * @return the regional summary DTO
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public RegionalSummaryDto getSummaryById(String id) {
        log.debug("Fetching regional summary by ID: {}", id);

        RegionalSummary entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("RegionalSummary not found with id: " + id));

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get RegionalSummary by region code and period ID.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return the regional summary DTO
     */
    @Cacheable(value = CACHE_NAME, key = "'region:' + #regionCode + ':period:' + #periodId")
    public RegionalSummaryDto getSummaryByRegionAndPeriod(String regionCode, String periodId) {
        log.debug("Fetching regional summary for region: {}, period: {}", regionCode, periodId);

        RegionalSummary entity = repository.findByRegionCodeAndPeriodId(regionCode, periodId)
            .orElseThrow(() -> new IllegalArgumentException(
                "RegionalSummary not found for region: " + regionCode + ", period: " + periodId));

        return enrichWithCalculatedMetrics(mapper.toDto(entity));
    }

    /**
     * Get all RegionalSummaries by period ID.
     *
     * @param periodId the period identifier
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesByPeriod(String periodId) {
        log.debug("Fetching regional summaries for period: {}", periodId);

        return repository.findByPeriodId(periodId).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get all RegionalSummaries by region code.
     *
     * @param regionCode the region code
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesByRegion(String regionCode) {
        log.debug("Fetching regional summaries for region: {}", regionCode);

        return repository.findByRegionCodeOrderByEndDateDesc(regionCode).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get all RegionalSummaries with pagination.
     *
     * @param pageable the pagination information
     * @return page of regional summary DTOs
     */
    public Page<RegionalSummaryDto> getAllSummaries(Pageable pageable) {
        log.debug("Fetching all regional summaries with pagination");

        Page<RegionalSummary> entities = repository.findAllByOrderByEndDateDesc(pageable);
        List<RegionalSummaryDto> dtos = entities.stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();

        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }

    /**
     * Get top performing regions by revenue.
     *
     * @param periodId the period identifier
     * @param limit    the maximum number of regions to return
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getTopPerformingRegions(String periodId, int limit) {
        log.debug("Fetching top {} performing regions for period: {}", limit, periodId);

        return repository.findByPeriodIdOrderByRevenueDesc(periodId,
            org.springframework.data.domain.PageRequest.of(0, limit)).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get regional summaries by growth rate threshold.
     *
     * @param minGrowthRate the minimum growth rate
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesByGrowthRate(BigDecimal minGrowthRate) {
        log.debug("Fetching regional summaries with growth rate >= {}", minGrowthRate);

        return repository.findByGrowthRateGreaterThanEqualOrderByGrowthRateDesc(minGrowthRate).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get regional summaries by customer satisfaction score.
     *
     * @param minScore the minimum satisfaction score
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesBySatisfactionScore(BigDecimal minScore) {
        log.debug("Fetching regional summaries with satisfaction score >= {}", minScore);

        return repository.findByCustomerSatisfactionScoreGreaterThanEqualOrderByCustomerSatisfactionScoreDesc(
            minScore).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get regional summaries by date range.
     *
     * @param regionCode the region code (optional, can be null for all regions)
     * @param startDate  the start date
     * @param endDate    the end date
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesByDateRange(String regionCode, LocalDateTime startDate,
        LocalDateTime endDate) {
        log.debug("Fetching regional summaries for date range: {} to {}", startDate, endDate);

        List<RegionalSummary> entities;
        if (regionCode != null) {
            entities = repository.findByRegionCodeAndStartDateBetweenOrderByEndDateDesc(
                regionCode, startDate, endDate);
        } else {
            entities = repository.findByStartDateBetweenOrderByEndDateDesc(startDate, endDate,
                org.springframework.data.domain.PageRequest.of(0, 1000)).toList();
        }

        return entities.stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Get regional summaries by status.
     *
     * @param status the summary status
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getSummariesByStatus(String status) {
        log.debug("Fetching regional summaries by status: {}", status);

        RegionalSummary.SummaryStatus summaryStatus =
            RegionalSummary.SummaryStatus.valueOf(status.toUpperCase());

        return repository.findByStatus(summaryStatus).stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }

    /**
     * Delete RegionalSummary by ID.
     *
     * @param id the summary ID
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteSummary(String id) {
        log.info("Deleting regional summary with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("RegionalSummary not found with id: " + id);
        }

        repository.deleteById(id);
        log.info("Deleted regional summary with ID: {}", id);
    }

    /**
     * Delete RegionalSummary by region code and period ID.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteSummaryByRegionAndPeriod(String regionCode, String periodId) {
        log.info("Deleting regional summary for region: {}, period: {}", regionCode, periodId);

        if (!repository.existsByRegionCodeAndPeriodId(regionCode, periodId)) {
            throw new IllegalArgumentException(
                "RegionalSummary not found for region: " + regionCode + ", period: " + periodId);
        }

        repository.deleteByRegionCodeAndPeriodId(regionCode, periodId);
        log.info("Deleted regional summary for region: {}, period: {}", regionCode, periodId);
    }

    /**
     * Aggregate regional summary from country metrics.
     *
     * @param countryMetrics list of country metrics for the region
     * @param regionCode      the region code
     * @param regionName      the region name
     * @param periodId        the period identifier
     * @return aggregated regional summary DTO
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public RegionalSummaryDto aggregateFromCountryMetrics(
        List<com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics> countryMetrics,
        String regionCode, String regionName, String periodId) {

        log.info("Aggregating regional summary for {} from {} countries", regionCode, countryMetrics.size());

        LocalDateTime startDate = countryMetrics.stream()
            .filter(m -> m.getStartDate() != null)
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getStartDate)
            .min(Comparator.naturalOrder())
            .orElse(LocalDateTime.now());

        LocalDateTime endDate = countryMetrics.stream()
            .filter(m -> m.getEndDate() != null)
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getEndDate)
            .max(Comparator.naturalOrder())
            .orElse(LocalDateTime.now());

        BigDecimal revenue = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getRevenue)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenses = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getExpenses)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long orderCount = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getOrderCount)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long customerCount = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getCustomerCount)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long newCustomers = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getNewCustomers)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        Long churnedCustomers = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getChurnedCustomers)
            .filter(java.util.Objects::nonNull)
            .reduce(0L, Long::sum);

        // Calculate average growth rate weighted by revenue
        BigDecimal weightedGrowthRate = calculateWeightedAverage(countryMetrics,
            com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getGrowthRate,
            com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getRevenue);

        // Calculate average market penetration
        BigDecimal avgMarketPenetration = countryMetrics.stream()
            .map(com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics::getMarketPenetration)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(countryMetrics.size()), 4, RoundingMode.HALF_UP);

        // Calculate average customer satisfaction
        BigDecimal avgSatisfaction = countryMetrics.stream()
            .filter(m -> m.getCustomerMetrics() != null && m.getCustomerMetrics().getSatisfactionScore() != null)
            .map(m -> m.getCustomerMetrics().getSatisfactionScore())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(
                BigDecimal.valueOf(
                    countryMetrics.stream()
                        .filter(m -> m.getCustomerMetrics() != null && m.getCustomerMetrics().getSatisfactionScore() != null)
                        .count()),
                2, RoundingMode.HALF_UP);

        // Build country metric summaries
        List<RegionalSummary.CountryMetricSummary> countrySummaries = countryMetrics.stream()
            .map(cm -> RegionalSummary.CountryMetricSummary.builder()
                .countryCode(cm.getCountryCode())
                .countryName(cm.getCountryName())
                .revenue(cm.getRevenue())
                .revenueContribution(revenue.compareTo(BigDecimal.ZERO) > 0 ?
                    cm.getRevenue().divide(revenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                    BigDecimal.ZERO)
                .growthRate(cm.getGrowthRate())
                .orders(cm.getOrderCount())
                .customers(cm.getCustomerCount())
                .profitMargin(cm.getProfitMargin())
                .marketShare(cm.getMarketShare())
                .build())
            .toList();

        RegionalSummary aggregated = RegionalSummary.builder()
            .regionCode(regionCode)
            .regionName(regionName)
            .periodId(periodId)
            .startDate(startDate)
            .endDate(endDate)
            .revenue(revenue)
            .expenses(expenses)
            .orderCount(orderCount)
            .customerCount(customerCount)
            .newCustomers(newCustomers)
            .churnedCustomers(churnedCustomers)
            .growthRate(weightedGrowthRate)
            .marketPenetration(avgMarketPenetration)
            .customerSatisfactionScore(avgSatisfaction)
            .countries(countrySummaries)
            .status(RegionalSummary.SummaryStatus.ACTIVE)
            .build();

        calculateDerivedMetrics(aggregated);
        aggregated = repository.save(aggregated);

        log.info("Created aggregated regional summary with ID: {}", aggregated.getId());
        return enrichWithCalculatedMetrics(mapper.toDto(aggregated));
    }

    /**
     * Calculate derived metrics for a RegionalSummary entity.
     *
     * @param summary the regional summary entity
     */
    private void calculateDerivedMetrics(RegionalSummary summary) {
        // Calculate profit
        if (summary.getRevenue() != null && summary.getExpenses() != null) {
            summary.setProfit(summary.getRevenue().subtract(summary.getExpenses()));
        }

        // Calculate profit margin
        if (summary.getRevenue() != null && summary.getRevenue().compareTo(BigDecimal.ZERO) > 0 &&
            summary.getProfit() != null) {
            summary.setProfitMargin(
                summary.getProfit().divide(summary.getRevenue(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        }

        // Set default base currency
        if (summary.getBaseCurrency() == null) {
            summary.setBaseCurrency("USD");
        }
    }

    /**
     * Enrich DTO with calculated metrics.
     *
     * @param dto the regional summary DTO
     * @return enriched regional summary DTO
     */
    private RegionalSummaryDto enrichWithCalculatedMetrics(RegionalSummaryDto dto) {
        if (dto == null) {
            return null;
        }

        RegionalSummaryDto.CalculatedMetrics calculated = RegionalSummaryDto.CalculatedMetrics.builder()
            .revenuePerCustomer(BigDecimal.ZERO)
            .ordersPerCustomer(BigDecimal.ZERO)
            .customerGrowthRate(BigDecimal.ZERO)
            .customerChurnRate(BigDecimal.ZERO)
            .netGrowthRate(BigDecimal.ZERO)
            .build();

        if (dto.getCustomerCount() != null && dto.getCustomerCount() > 0) {
            if (dto.getRevenue() != null) {
                calculated.setRevenuePerCustomer(
                    dto.getRevenue().divide(BigDecimal.valueOf(dto.getCustomerCount()), 2, RoundingMode.HALF_UP));
            }

            if (dto.getOrderCount() != null) {
                calculated.setOrdersPerCustomer(
                    BigDecimal.valueOf(dto.getOrderCount()).divide(BigDecimal.valueOf(dto.getCustomerCount()), 2, RoundingMode.HALF_UP));
            }

            if (dto.getNewCustomers() != null) {
                calculated.setCustomerGrowthRate(
                    BigDecimal.valueOf(dto.getNewCustomers()).divide(BigDecimal.valueOf(dto.getCustomerCount()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            }

            if (dto.getChurnedCustomers() != null) {
                calculated.setCustomerChurnRate(
                    BigDecimal.valueOf(dto.getChurnedCustomers()).divide(BigDecimal.valueOf(dto.getCustomerCount()), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            }

            BigDecimal netGrowth = calculated.getCustomerGrowthRate().subtract(calculated.getCustomerChurnRate());
            calculated.setNetGrowthRate(netGrowth);
        }

        dto.setCalculatedMetrics(calculated);
        return dto;
    }

    /**
     * Calculate weighted average.
     */
    private <T> BigDecimal calculateWeightedAverage(List<T> items,
        java.util.function.Function<T, BigDecimal> valueExtractor,
        java.util.function.Function<T, BigDecimal> weightExtractor) {

        BigDecimal totalWeight = items.stream()
            .map(weightExtractor)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return items.stream()
            .map(item -> {
                BigDecimal value = valueExtractor.apply(item);
                BigDecimal weight = weightExtractor.apply(item);
                if (value != null && weight != null) {
                    return value.multiply(weight);
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(totalWeight, 4, RoundingMode.HALF_UP);
    }

    /**
     * Check if summary exists for region and period.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return true if summary exists, false otherwise
     */
    public boolean existsByRegionAndPeriod(String regionCode, String periodId) {
        return repository.existsByRegionCodeAndPeriodId(regionCode, periodId);
    }

    /**
     * Count summaries by period.
     *
     * @param periodId the period identifier
     * @return count of summaries
     */
    public long countByPeriod(String periodId) {
        return repository.countByPeriodId(periodId);
    }

    /**
     * Get regions with positive customer growth.
     *
     * @return list of regional summary DTOs
     */
    public List<RegionalSummaryDto> getRegionsWithPositiveCustomerGrowth() {
        return repository.findWithPositiveCustomerGrowth().stream()
            .map(mapper::toDto)
            .map(this::enrichWithCalculatedMetrics)
            .toList();
    }
}
