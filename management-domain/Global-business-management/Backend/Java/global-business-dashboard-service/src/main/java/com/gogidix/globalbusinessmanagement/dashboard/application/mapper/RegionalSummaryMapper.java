package com.gogidix.globalbusinessmanagement.dashboard.application.mapper;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for RegionalSummary domain model and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RegionalSummaryMapper {

    RegionalSummaryDto toDto(RegionalSummary entity);

    RegionalSummary toEntity(RegionalSummaryDto dto);

    List<RegionalSummaryDto> toDtoList(List<RegionalSummary> entities);

    List<RegionalSummary> toEntityList(List<RegionalSummaryDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(RegionalSummaryDto dto, @MappingTarget RegionalSummary entity);

    default RegionalSummaryDto.CountryMetricSummaryDto toCountryMetricSummaryDto(
            RegionalSummary.CountryMetricSummary summary) {
        if (summary == null) {
            return null;
        }
        return RegionalSummaryDto.CountryMetricSummaryDto.builder()
            .countryCode(summary.getCountryCode())
            .countryName(summary.getCountryName())
            .revenue(summary.getRevenue())
            .revenueContribution(summary.getRevenueContribution())
            .growthRate(summary.getGrowthRate())
            .orders(summary.getOrders())
            .customers(summary.getCustomers())
            .profitMargin(summary.getProfitMargin())
            .marketShare(summary.getMarketShare())
            .build();
    }

    default RegionalSummary.CountryMetricSummary toCountryMetricSummary(
            RegionalSummaryDto.CountryMetricSummaryDto dto) {
        if (dto == null) {
            return null;
        }
        return RegionalSummary.CountryMetricSummary.builder()
            .countryCode(dto.getCountryCode())
            .countryName(dto.getCountryName())
            .revenue(dto.getRevenue())
            .revenueContribution(dto.getRevenueContribution())
            .growthRate(dto.getGrowthRate())
            .orders(dto.getOrders())
            .customers(dto.getCustomers())
            .profitMargin(dto.getProfitMargin())
            .marketShare(dto.getMarketShare())
            .build();
    }

    default RegionalSummaryDto.ProductPerformanceDto toProductPerformanceDto(
            RegionalSummary.ProductPerformance performance) {
        if (performance == null) {
            return null;
        }
        return RegionalSummaryDto.ProductPerformanceDto.builder()
            .productCode(performance.getProductCode())
            .productName(performance.getProductName())
            .category(performance.getCategory())
            .revenue(performance.getRevenue())
            .unitsSold(performance.getUnitsSold())
            .growthRate(performance.getGrowthRate())
            .marketShare(performance.getMarketShare())
            .build();
    }

    default RegionalSummary.ProductPerformance toProductPerformance(
            RegionalSummaryDto.ProductPerformanceDto dto) {
        if (dto == null) {
            return null;
        }
        return RegionalSummary.ProductPerformance.builder()
            .productCode(dto.getProductCode())
            .productName(dto.getProductName())
            .category(dto.getCategory())
            .revenue(dto.getRevenue())
            .unitsSold(dto.getUnitsSold())
            .growthRate(dto.getGrowthRate())
            .marketShare(dto.getMarketShare())
            .build();
    }

    default RegionalSummaryDto.RegionalTrendsDto toRegionalTrendsDto(
            RegionalSummary.RegionalTrends trends) {
        if (trends == null) {
            return null;
        }
        return RegionalSummaryDto.RegionalTrendsDto.builder()
            .revenueTrend(trends.getRevenueTrend())
            .revenueTrendDirection(trends.getRevenueTrendDirection())
            .customerTrend(trends.getCustomerTrend())
            .customerTrendDirection(trends.getCustomerTrendDirection())
            .orderTrend(trends.getOrderTrend())
            .orderTrendDirection(trends.getOrderTrendDirection())
            .topGrowthDriver(trends.getTopGrowthDriver())
            .topRiskFactor(trends.getTopRiskFactor())
            .build();
    }

    default RegionalSummary.RegionalTrends toRegionalTrends(
            RegionalSummaryDto.RegionalTrendsDto dto) {
        if (dto == null) {
            return null;
        }
        return RegionalSummary.RegionalTrends.builder()
            .revenueTrend(dto.getRevenueTrend())
            .revenueTrendDirection(dto.getRevenueTrendDirection())
            .customerTrend(dto.getCustomerTrend())
            .customerTrendDirection(dto.getCustomerTrendDirection())
            .orderTrend(dto.getOrderTrend())
            .orderTrendDirection(dto.getOrderTrendDirection())
            .topGrowthDriver(dto.getTopGrowthDriver())
            .topRiskFactor(dto.getTopRiskFactor())
            .build();
    }

    default RegionalSummaryDto.RegionalDemographicsDto toRegionalDemographicsDto(
            RegionalSummary.RegionalDemographics demographics) {
        if (demographics == null) {
            return null;
        }
        return RegionalSummaryDto.RegionalDemographicsDto.builder()
            .totalPopulation(demographics.getTotalPopulation())
            .targetMarketSize(demographics.getTargetMarketSize())
            .urbanizationRate(demographics.getUrbanizationRate())
            .averageIncome(demographics.getAverageIncome())
            .dominantLanguage(demographics.getDominantLanguage())
            .supportedLanguages(demographics.getSupportedLanguages())
            .primaryCurrency(demographics.getPrimaryCurrency())
            .acceptedCurrencies(demographics.getAcceptedCurrencies())
            .build();
    }

    default RegionalSummary.RegionalDemographics toRegionalDemographics(
            RegionalSummaryDto.RegionalDemographicsDto dto) {
        if (dto == null) {
            return null;
        }
        return RegionalSummary.RegionalDemographics.builder()
            .totalPopulation(dto.getTotalPopulation())
            .targetMarketSize(dto.getTargetMarketSize())
            .urbanizationRate(dto.getUrbanizationRate())
            .averageIncome(dto.getAverageIncome())
            .dominantLanguage(dto.getDominantLanguage())
            .supportedLanguages(dto.getSupportedLanguages())
            .primaryCurrency(dto.getPrimaryCurrency())
            .acceptedCurrencies(dto.getAcceptedCurrencies())
            .build();
    }

    default String mapSummaryStatus(RegionalSummary.SummaryStatus status) {
        return status != null ? status.name() : null;
    }

    default RegionalSummary.SummaryStatus mapSummaryStatus(String status) {
        return status != null ? RegionalSummary.SummaryStatus.valueOf(status) : null;
    }
}
