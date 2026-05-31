package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.AggregationResponseDto;
import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
import com.gogidix.sales.dashboard.domain.repository.SalesAggregationRepository;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Aggregation Query Service
 * Handles all read operations for sales aggregations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationQueryService {

    private final SalesAggregationRepository aggregationRepository;

    public List<AggregationResponseDto> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all aggregations for tenant: {}", tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findByTenantId(tenantId);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "aggregations", key = "#aggregationId")
    public AggregationResponseDto getById(String aggregationId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching aggregation: {} for tenant: {}", aggregationId, tenantId);

        SalesAggregation aggregation = aggregationRepository.findByAggregationIdAndTenantId(aggregationId, tenantId)
                .orElseThrow(() -> new NotFoundException("Aggregation", aggregationId));

        return toDto(aggregation);
    }

    public List<AggregationResponseDto> getByType(SalesAggregation.AggregationType type) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching aggregations by type: {} for tenant: {}", type, tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findByTenantIdAndAggregationType(tenantId, type);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AggregationResponseDto> getByDimension(SalesAggregation.AggregationDimension dimension) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching aggregations by dimension: {} for tenant: {}", dimension, tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findByTenantIdAndDimension(tenantId, dimension);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AggregationResponseDto> getByDimensionAndValue(
            SalesAggregation.AggregationDimension dimension, String value) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching aggregations by dimension: {} and value: {} for tenant: {}", dimension, value, tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findByTenantIdAndDimensionAndValue(
                tenantId, dimension, value);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AggregationResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching aggregations from {} to {} for tenant: {}", startDate, endDate, tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findByTenantIdAndDateRange(tenantId, startDate, endDate);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AggregationResponseDto> getLatestByType(SalesAggregation.AggregationType type, int limit) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching latest {} aggregations of type: {} for tenant: {}", limit, type, tenantId);

        List<SalesAggregation> aggregations = aggregationRepository.findLatestByTenantIdAndType(tenantId, type, limit);
        return aggregations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> comparePeriods(
            SalesAggregation.AggregationType type,
            SalesAggregation.AggregationDimension dimension,
            LocalDate period1Start, LocalDate period1End,
            LocalDate period2Start, LocalDate period2End) {

        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Comparing aggregations for periods {} to {} vs {} to {}",
                period1Start, period1End, period2Start, period2End);

        List<SalesAggregation> period1Aggregations = aggregationRepository.findByTenantIdAndDateRange(
                tenantId, period1Start, period1End);
        List<SalesAggregation> period2Aggregations = aggregationRepository.findByTenantIdAndDateRange(
                tenantId, period2Start, period2End);

        Map<String, Object> result = new HashMap<>();

        BigDecimal period1Revenue = period1Aggregations.stream()
                .filter(a -> a.getMetrics() != null && a.getMetrics().getTotalRevenue() != null)
                .map(a -> a.getMetrics().getTotalRevenue().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal period2Revenue = period2Aggregations.stream()
                .filter(a -> a.getMetrics() != null && a.getMetrics().getTotalRevenue() != null)
                .map(a -> a.getMetrics().getTotalRevenue().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("period1Revenue", period1Revenue);
        result.put("period2Revenue", period2Revenue);

        if (period2Revenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal growthRate = period1Revenue.subtract(period2Revenue)
                    .divide(period2Revenue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            result.put("growthRate", growthRate);
        }

        int period1Deals = period1Aggregations.stream()
                .filter(a -> a.getMetrics() != null)
                .mapToInt(a -> a.getMetrics().getTotalDeals() != null ? a.getMetrics().getTotalDeals() : 0)
                .sum();

        int period2Deals = period2Aggregations.stream()
                .filter(a -> a.getMetrics() != null)
                .mapToInt(a -> a.getMetrics().getTotalDeals() != null ? a.getMetrics().getTotalDeals() : 0)
                .sum();

        result.put("period1Deals", period1Deals);
        result.put("period2Deals", period2Deals);

        return result;
    }

    public AggregationResponseDto toDto(SalesAggregation aggregation) {
        return AggregationResponseDto.builder()
                .id(aggregation.getId())
                .aggregationId(aggregation.getAggregationId())
                .tenantId(aggregation.getTenantId())
                .aggregationType(aggregation.getAggregationType().name())
                .dimension(aggregation.getDimension().name())
                .dimensionValue(aggregation.getDimensionValue())
                .startDate(aggregation.getTimePeriod().getStartDate())
                .endDate(aggregation.getTimePeriod().getEndDate())
                .periodType(aggregation.getTimePeriod().getPeriodType())
                .totalRevenue(toMoneyDto(aggregation.getMetrics() != null ? aggregation.getMetrics().getTotalRevenue() : null))
                .targetRevenue(toMoneyDto(aggregation.getMetrics() != null ? aggregation.getMetrics().getTargetRevenue() : null))
                .achievementPercentage(aggregation.getMetrics() != null ?
                        toDouble(aggregation.getMetrics().getAchievementPercentage()) : null)
                .totalDeals(aggregation.getMetrics() != null ? aggregation.getMetrics().getTotalDeals() : 0)
                .wonDeals(aggregation.getMetrics() != null ? aggregation.getMetrics().getWonDeals() : 0)
                .winRate(aggregation.getMetrics() != null ? toDouble(aggregation.getMetrics().getWinRate()) : null)
                .averageDealSize(toMoneyDto(aggregation.getMetrics() != null ? aggregation.getMetrics().getAverageDealSize() : null))
                .isComplete(aggregation.getIsComplete())
                .aggregationTime(aggregation.getAggregationTime())
                .createdAt(aggregation.getCreatedAt())
                .updatedAt(aggregation.getUpdatedAt())
                .build();
    }

    private AggregationResponseDto.MoneyDto toMoneyDto(SalesAggregation.Money money) {
        if (money == null) {
            return null;
        }
        return AggregationResponseDto.MoneyDto.builder()
                .amount(toDouble(money.getAmount()))
                .currency(money.getCurrency())
                .build();
    }

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
