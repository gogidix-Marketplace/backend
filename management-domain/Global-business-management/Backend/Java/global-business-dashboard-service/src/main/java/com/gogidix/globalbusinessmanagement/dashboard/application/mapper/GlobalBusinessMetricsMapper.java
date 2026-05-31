package com.gogidix.globalbusinessmanagement.dashboard.application.mapper;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.GlobalBusinessMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for GlobalBusinessMetrics domain model and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GlobalBusinessMetricsMapper {

    GlobalBusinessMetricsDto toDto(GlobalBusinessMetrics entity);

    GlobalBusinessMetrics toEntity(GlobalBusinessMetricsDto dto);

    List<GlobalBusinessMetricsDto> toDtoList(List<GlobalBusinessMetrics> entities);

    List<GlobalBusinessMetrics> toEntityList(List<GlobalBusinessMetricsDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(GlobalBusinessMetricsDto dto, @MappingTarget GlobalBusinessMetrics entity);

    default GlobalBusinessMetricsDto.RegionalContributionDto toRegionalContributionDto(
            GlobalBusinessMetrics.RegionalContribution contribution) {
        if (contribution == null) {
            return null;
        }
        return GlobalBusinessMetricsDto.RegionalContributionDto.builder()
            .regionCode(contribution.getRegionCode())
            .regionName(contribution.getRegionName())
            .revenue(contribution.getRevenue())
            .revenueContribution(contribution.getRevenueContribution())
            .growthRate(contribution.getGrowthRate())
            .orders(contribution.getOrders())
            .customers(contribution.getCustomers())
            .profitMargin(contribution.getProfitMargin())
            .build();
    }

    default GlobalBusinessMetrics.RegionalContribution toRegionalContribution(
            GlobalBusinessMetricsDto.RegionalContributionDto dto) {
        if (dto == null) {
            return null;
        }
        return GlobalBusinessMetrics.RegionalContribution.builder()
            .regionCode(dto.getRegionCode())
            .regionName(dto.getRegionName())
            .revenue(dto.getRevenue())
            .revenueContribution(dto.getRevenueContribution())
            .growthRate(dto.getGrowthRate())
            .orders(dto.getOrders())
            .customers(dto.getCustomers())
            .profitMargin(dto.getProfitMargin())
            .build();
    }

    default GlobalBusinessMetricsDto.CategoryPerformanceDto toCategoryPerformanceDto(
            GlobalBusinessMetrics.CategoryPerformance performance) {
        if (performance == null) {
            return null;
        }
        return GlobalBusinessMetricsDto.CategoryPerformanceDto.builder()
            .categoryCode(performance.getCategoryCode())
            .categoryName(performance.getCategoryName())
            .revenue(performance.getRevenue())
            .revenueContribution(performance.getRevenueContribution())
            .growthRate(performance.getGrowthRate())
            .unitsSold(performance.getUnitsSold())
            .averagePrice(performance.getAveragePrice())
            .productCount(performance.getProductCount())
            .build();
    }

    default GlobalBusinessMetrics.CategoryPerformance toCategoryPerformance(
            GlobalBusinessMetricsDto.CategoryPerformanceDto dto) {
        if (dto == null) {
            return null;
        }
        return GlobalBusinessMetrics.CategoryPerformance.builder()
            .categoryCode(dto.getCategoryCode())
            .categoryName(dto.getCategoryName())
            .revenue(dto.getRevenue())
            .revenueContribution(dto.getRevenueContribution())
            .growthRate(dto.getGrowthRate())
            .unitsSold(dto.getUnitsSold())
            .averagePrice(dto.getAveragePrice())
            .productCount(dto.getProductCount())
            .build();
    }

    default GlobalBusinessMetricsDto.MarketTrendsDto toMarketTrendsDto(
            GlobalBusinessMetrics.MarketTrends trends) {
        if (trends == null) {
            return null;
        }
        return GlobalBusinessMetricsDto.MarketTrendsDto.builder()
            .marketShare(trends.getMarketShare())
            .marketGrowthRate(trends.getMarketGrowthRate())
            .competitorCount(trends.getCompetitorCount())
            .industryAverageMargin(trends.getIndustryAverageMargin())
            .trendDirection(trends.getTrendDirection())
            .build();
    }

    default GlobalBusinessMetrics.MarketTrends toMarketTrends(
            GlobalBusinessMetricsDto.MarketTrendsDto dto) {
        if (dto == null) {
            return null;
        }
        return GlobalBusinessMetrics.MarketTrends.builder()
            .marketShare(dto.getMarketShare())
            .marketGrowthRate(dto.getMarketGrowthRate())
            .competitorCount(dto.getCompetitorCount())
            .industryAverageMargin(dto.getIndustryAverageMargin())
            .trendDirection(dto.getTrendDirection())
            .build();
    }

    default GlobalBusinessMetricsDto.OperationalMetricsDto toOperationalMetricsDto(
            GlobalBusinessMetrics.OperationalMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return GlobalBusinessMetricsDto.OperationalMetricsDto.builder()
            .inventoryTurnover(metrics.getInventoryTurnover())
            .orderFulfillmentRate(metrics.getOrderFulfillmentRate())
            .returnRate(metrics.getReturnRate())
            .averageFulfillmentTime(metrics.getAverageFulfillmentTime())
            .activeProducts(metrics.getActiveProducts())
            .discontinuedProducts(metrics.getDiscontinuedProducts())
            .build();
    }

    default GlobalBusinessMetrics.OperationalMetrics toOperationalMetrics(
            GlobalBusinessMetricsDto.OperationalMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return GlobalBusinessMetrics.OperationalMetrics.builder()
            .inventoryTurnover(dto.getInventoryTurnover())
            .orderFulfillmentRate(dto.getOrderFulfillmentRate())
            .returnRate(dto.getReturnRate())
            .averageFulfillmentTime(dto.getAverageFulfillmentTime())
            .activeProducts(dto.getActiveProducts())
            .discontinuedProducts(dto.getDiscontinuedProducts())
            .build();
    }

    default String mapMetricsStatus(GlobalBusinessMetrics.MetricsStatus status) {
        return status != null ? status.name() : null;
    }

    default GlobalBusinessMetrics.MetricsStatus mapMetricsStatus(String status) {
        return status != null ? GlobalBusinessMetrics.MetricsStatus.valueOf(status) : null;
    }
}
