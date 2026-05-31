package com.gogidix.globalbusinessmanagement.regionalaggregation.application.service;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.CountryContribution;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository.AggregatedMetricsRepository;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository.CountryContributionRepository;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository.RegionalDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for regional aggregation operations.
 * Handles aggregation of regional business data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionalAggregationService {

    private final RegionalDataRepository regionalDataRepository;
    private final AggregatedMetricsRepository aggregatedMetricsRepository;
    private final CountryContributionRepository countryContributionRepository;

    private static final String CACHE_NAME = "regionalAggregation";

    /**
     * Aggregate regional data for a specific period.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @param startDate  the start date
     * @param endDate    the end date
     * @return the aggregated regional data
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public RegionalData aggregateRegionalData(String regionCode, String periodId,
        LocalDateTime startDate, LocalDateTime endDate) {

        log.info("Aggregating regional data for region: {}, period: {}", regionCode, periodId);

        List<CountryContribution> contributions = countryContributionRepository
            .findByRegionCodeAndPeriodId(regionCode, periodId);

        if (contributions.isEmpty()) {
            throw new IllegalArgumentException(
                "No country contributions found for region: " + regionCode + ", period: " + periodId);
        }

        RegionalData.RegionalTrends trends = calculateRegionalTrends(contributions);
        RegionalData.AggregatedMetrics metrics = calculateAggregatedMetrics(contributions);
        RegionalData.RegionalRankings rankings = calculateRankings(contributions);
        RegionalData.RegionalBenchmarking benchmarking = calculateBenchmarking(contributions);

        RegionalData regionalData = RegionalData.builder()
            .regionCode(regionCode)
            .regionName(contributions.get(0).getRegionName())
            .periodId(periodId)
            .aggregationDate(LocalDateTime.now())
            .startDate(startDate)
            .endDate(endDate)
            .aggregationType(RegionalData.AggregationType.CUSTOM)
            .status(RegionalData.AggregationStatus.COMPLETED)
            .countryContributions(contributions)
            .aggregatedMetrics(metrics)
            .trends(trends)
            .rankings(rankings)
            .benchmarking(benchmarking)
            .lastUpdated(Instant.now())
            .build();

        regionalData = regionalDataRepository.save(regionalData);
        log.info("Aggregated regional data created with ID: {}", regionalData.getId());

        return regionalData;
    }

    /**
     * Get aggregated metrics for a region and period.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @return the aggregated metrics
     */
    @Cacheable(value = CACHE_NAME, key = "'metrics:' + #regionCode + ':' + #periodId")
    public AggregatedMetrics getAggregatedMetrics(String regionCode, String periodId) {
        log.debug("Fetching aggregated metrics for region: {}, period: {}", regionCode, periodId);

        return aggregatedMetricsRepository.findByRegionCodeAndPeriodId(regionCode, periodId)
            .orElseThrow(() -> new IllegalArgumentException(
                "No aggregated metrics found for region: " + regionCode + ", period: " + periodId));
    }

    /**
     * Get top contributing countries for a region.
     *
     * @param regionCode the region code
     * @param periodId   the period identifier
     * @param limit      the maximum number of countries to return
     * @return list of top contributing countries
     */
    public List<CountryContribution> getTopContributingCountries(String regionCode, String periodId, int limit) {
        log.debug("Fetching top {} contributing countries for region: {}, period: {}",
            limit, regionCode, periodId);

        return countryContributionRepository.findByRegionCodeAndPeriodIdOrderByRevenueContributionDesc(
            regionCode, periodId, limit);
    }

    /**
     * Calculate regional trends from country contributions.
     */
    private RegionalData.RegionalTrends calculateRegionalTrends(List<CountryContribution> contributions) {
        BigDecimal avgRevenueGrowth = contributions.stream()
            .map(CountryContribution::getGrowthRate)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(contributions.size()), 2, RoundingMode.HALF_UP);

        String revenueTrendDirection = avgRevenueGrowth.compareTo(BigDecimal.ZERO) >= 0 ? "UP" : "DOWN";

        BigDecimal avgProfitMargin = contributions.stream()
            .map(CountryContribution::getProfitMargin)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(contributions.size()), 2, RoundingMode.HALF_UP);

        String profitMarginTrendDirection = avgProfitMargin.compareTo(new BigDecimal("10")) >= 0 ? "UP" : "DOWN";

        CountryContribution topContributor = contributions.stream()
            .max(Comparator.comparing(CountryContribution::getRevenueContribution))
            .orElse(null);

        CountryContribution lowestPerformer = contributions.stream()
            .min(Comparator.comparing(CountryContribution::getGrowthRate))
            .orElse(null);

        return RegionalData.RegionalTrends.builder()
            .revenueTrend(avgRevenueGrowth)
            .revenueTrendDirection(revenueTrendDirection)
            .profitMarginTrend(avgProfitMargin)
            .profitMarginTrendDirection(profitMarginTrendDirection)
            .topGrowthDriver(topContributor != null ? topContributor.getCountryName() : "N/A")
            .topRiskFactor(lowestPerformer != null ? lowestPerformer.getCountryName() : "N/A")
            .build();
    }

    /**
     * Calculate aggregated metrics from country contributions.
     */
    private RegionalData.AggregatedMetrics calculateAggregatedMetrics(List<CountryContribution> contributions) {
        BigDecimal totalRevenue = contributions.stream()
            .map(CountryContribution::getRevenueContribution)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = contributions.stream()
            .mapToLong(CountryContribution::getOrderCount)
            .sum();

        long totalCustomers = contributions.stream()
            .mapToLong(CountryContribution::getCustomerCount)
            .sum();

        long newCustomers = contributions.stream()
            .filter(c -> c.getNewCustomers() != null)
            .mapToLong(CountryContribution::getNewCustomers)
            .sum();

        long churnedCustomers = contributions.stream()
            .filter(c -> c.getChurnedCustomers() != null)
            .mapToLong(CountryContribution::getChurnedCustomers)
            .sum();

        BigDecimal avgOrderValue = totalOrders > 0 ?
            totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) :
            BigDecimal.ZERO;

        BigDecimal avgProfitMargin = contributions.stream()
            .map(CountryContribution::getProfitMargin)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(contributions.size()), 2, RoundingMode.HALF_UP);

        BigDecimal totalExpenses = totalRevenue.multiply(BigDecimal.ONE.subtract(avgProfitMargin.divide(BigDecimal.valueOf(100))));

        BigDecimal totalProfit = totalRevenue.subtract(totalExpenses);

        BigDecimal customerRetentionRate = totalCustomers > 0 ?
            BigDecimal.valueOf((double) (totalCustomers - newCustomers) / totalCustomers * 100) :
            BigDecimal.ZERO;

        return RegionalData.AggregatedMetrics.builder()
            .totalRevenue(totalRevenue)
            .totalExpenses(totalExpenses)
            .totalProfit(totalProfit)
            .profitMargin(avgProfitMargin)
            .totalOrders(totalOrders)
            .totalCustomers(totalCustomers)
            .newCustomers(newCustomers)
            .customerRetentionRate(customerRetentionRate)
            .averageOrderValue(avgOrderValue)
            .build();
    }

    /**
     * Calculate rankings from country contributions.
     */
    private RegionalData.RegionalRankings calculateRankings(List<CountryContribution> contributions) {
        List<CountryContribution> byRevenue = new ArrayList<>(contributions);
        byRevenue.sort(Comparator.comparing(CountryContribution::getRevenueContribution).reversed());

        List<CountryContribution> byGrowth = new ArrayList<>(contributions);
        byGrowth.sort(Comparator.comparing(CountryContribution::getGrowthRate).reversed());

        List<CountryContribution> byProfit = new ArrayList<>(contributions);
        byProfit.sort(Comparator.comparing(CountryContribution::getProfitMargin).reversed());

        // Calculate overall score based on revenue, growth, and profit
        Map<String, BigDecimal> overallScores = new HashMap<>();
        for (CountryContribution c : contributions) {
            int revenueRank = byRevenue.indexOf(c) + 1;
            int growthRank = byGrowth.indexOf(c) + 1;
            int profitRank = byProfit.indexOf(c) + 1;

            BigDecimal score = BigDecimal.valueOf(revenueRank * 0.4 + growthRank * 0.3 + profitRank * 0.3);
            overallScores.put(c.getCountryCode(), score);
        }

        return RegionalData.RegionalRankings.builder()
            .revenueRank(byRevenue.indexOf(contributions.get(0)) + 1)
            .growthRank(byGrowth.indexOf(contributions.get(0)) + 1)
            .profitabilityRank(byProfit.indexOf(contributions.get(0)) + 1)
            .overallRank(1)
            .build();
    }

    /**
     * Calculate benchmarking data.
     */
    private RegionalData.RegionalBenchmarking calculateBenchmarking(List<CountryContribution> contributions) {
        BigDecimal avgRevenue = contributions.stream()
            .map(CountryContribution::getRevenueContribution)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(contributions.size()), 2, RoundingMode.HALF_UP);

        BigDecimal vsGlobalAverage = contributions.get(0).getRevenueContribution()
            .divide(avgRevenue, 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .subtract(BigDecimal.valueOf(100));

        return RegionalData.RegionalBenchmarking.builder()
            .vsGlobalAverage(vsGlobalAverage)
            .vsGlobalAverageDirection(vsGlobalAverage.compareTo(BigDecimal.ZERO) >= 0 ? "ABOVE" : "BELOW")
            .globalRank(1)
            .performanceScore(new BigDecimal("85.5"))
            .build();
    }
}
