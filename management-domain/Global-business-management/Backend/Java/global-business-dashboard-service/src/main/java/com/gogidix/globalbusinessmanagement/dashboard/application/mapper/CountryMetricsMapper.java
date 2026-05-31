package com.gogidix.globalbusinessmanagement.dashboard.application.mapper;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.CountryMetricsDto;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for CountryMetrics domain model and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CountryMetricsMapper {

    CountryMetricsDto toDto(CountryMetrics entity);

    CountryMetrics toEntity(CountryMetricsDto dto);

    List<CountryMetricsDto> toDtoList(List<CountryMetrics> entities);

    List<CountryMetrics> toEntityList(List<CountryMetricsDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CountryMetricsDto dto, @MappingTarget CountryMetrics entity);

    default CountryMetricsDto.ProductLineMetricsDto toProductLineMetricsDto(
            CountryMetrics.ProductLineMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return CountryMetricsDto.ProductLineMetricsDto.builder()
            .productLineCode(metrics.getProductLineCode())
            .productLineName(metrics.getProductLineName())
            .revenue(metrics.getRevenue())
            .revenueContribution(metrics.getRevenueContribution())
            .unitsSold(metrics.getUnitsSold())
            .growthRate(metrics.getGrowthRate())
            .profitMargin(metrics.getProfitMargin())
            .productCount(metrics.getProductCount())
            .build();
    }

    default CountryMetrics.ProductLineMetrics toProductLineMetrics(
            CountryMetricsDto.ProductLineMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.ProductLineMetrics.builder()
            .productLineCode(dto.getProductLineCode())
            .productLineName(dto.getProductLineName())
            .revenue(dto.getRevenue())
            .revenueContribution(dto.getRevenueContribution())
            .unitsSold(dto.getUnitsSold())
            .growthRate(dto.getGrowthRate())
            .profitMargin(dto.getProfitMargin())
            .productCount(dto.getProductCount())
            .build();
    }

    default CountryMetricsDto.CityMetricsDto toCityMetricsDto(CountryMetrics.CityMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return CountryMetricsDto.CityMetricsDto.builder()
            .cityName(metrics.getCityName())
            .regionCode(metrics.getRegionCode())
            .revenue(metrics.getRevenue())
            .customers(metrics.getCustomers())
            .orders(metrics.getOrders())
            .revenueContribution(metrics.getRevenueContribution())
            .growthRate(metrics.getGrowthRate())
            .build();
    }

    default CountryMetrics.CityMetrics toCityMetrics(CountryMetricsDto.CityMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.CityMetrics.builder()
            .cityName(dto.getCityName())
            .regionCode(dto.getRegionCode())
            .revenue(dto.getRevenue())
            .customers(dto.getCustomers())
            .orders(dto.getOrders())
            .revenueContribution(dto.getRevenueContribution())
            .growthRate(dto.getGrowthRate())
            .build();
    }

    default CountryMetricsDto.SalesChannelMetricsDto toSalesChannelMetricsDto(
            CountryMetrics.SalesChannelMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return CountryMetricsDto.SalesChannelMetricsDto.builder()
            .onlineRevenue(metrics.getOnlineRevenue())
            .offlineRevenue(metrics.getOfflineRevenue())
            .marketplaceRevenue(metrics.getMarketplaceRevenue())
            .b2bRevenue(metrics.getB2bRevenue())
            .onlineContribution(metrics.getOnlineContribution())
            .offlineContribution(metrics.getOfflineContribution())
            .onlineOrders(metrics.getOnlineOrders())
            .offlineOrders(metrics.getOfflineOrders())
            .marketplaceOrders(metrics.getMarketplaceOrders())
            .build();
    }

    default CountryMetrics.SalesChannelMetrics toSalesChannelMetrics(
            CountryMetricsDto.SalesChannelMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.SalesChannelMetrics.builder()
            .onlineRevenue(dto.getOnlineRevenue())
            .offlineRevenue(dto.getOfflineRevenue())
            .marketplaceRevenue(dto.getMarketplaceRevenue())
            .b2bRevenue(dto.getB2bRevenue())
            .onlineContribution(dto.getOnlineContribution())
            .offlineContribution(dto.getOfflineContribution())
            .onlineOrders(dto.getOnlineOrders())
            .offlineOrders(dto.getOfflineOrders())
            .marketplaceOrders(dto.getMarketplaceOrders())
            .build();
    }

    default CountryMetricsDto.CustomerMetricsDto toCustomerMetricsDto(
            CountryMetrics.CustomerMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return CountryMetricsDto.CustomerMetricsDto.builder()
            .averageAge(metrics.getAverageAge())
            .genderDistribution(metrics.getGenderDistribution())
            .ageGroupDistribution(metrics.getAgeGroupDistribution())
            .satisfactionScore(metrics.getSatisfactionScore())
            .netPromoterScore(metrics.getNetPromoterScore())
            .repeatPurchaseRate(metrics.getRepeatPurchaseRate())
            .averageSessionDuration(metrics.getAverageSessionDuration())
            .averagePagesPerSession(metrics.getAveragePagesPerSession())
            .bounceRate(metrics.getBounceRate())
            .build();
    }

    default CountryMetrics.CustomerMetrics toCustomerMetrics(
            CountryMetricsDto.CustomerMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.CustomerMetrics.builder()
            .averageAge(dto.getAverageAge())
            .genderDistribution(dto.getGenderDistribution())
            .ageGroupDistribution(dto.getAgeGroupDistribution())
            .satisfactionScore(dto.getSatisfactionScore())
            .netPromoterScore(dto.getNetPromoterScore())
            .repeatPurchaseRate(dto.getRepeatPurchaseRate())
            .averageSessionDuration(dto.getAverageSessionDuration())
            .averagePagesPerSession(dto.getAveragePagesPerSession())
            .bounceRate(dto.getBounceRate())
            .build();
    }

    default CountryMetricsDto.OperationalMetricsDto toOperationalMetricsDto(
            CountryMetrics.OperationalMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return CountryMetricsDto.OperationalMetricsDto.builder()
            .inventoryTurnover(metrics.getInventoryTurnover())
            .fulfillmentRate(metrics.getFulfillmentRate())
            .onTimeDeliveryRate(metrics.getOnTimeDeliveryRate())
            .returnRate(metrics.getReturnRate())
            .refundRate(metrics.getRefundRate())
            .averageFulfillmentTime(metrics.getAverageFulfillmentTime())
            .averageResponseTime(metrics.getAverageResponseTime())
            .firstContactResolution(metrics.getFirstContactResolution())
            .build();
    }

    default CountryMetrics.OperationalMetrics toOperationalMetrics(
            CountryMetricsDto.OperationalMetricsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.OperationalMetrics.builder()
            .inventoryTurnover(dto.getInventoryTurnover())
            .fulfillmentRate(dto.getFulfillmentRate())
            .onTimeDeliveryRate(dto.getOnTimeDeliveryRate())
            .returnRate(dto.getReturnRate())
            .refundRate(dto.getRefundRate())
            .averageFulfillmentTime(dto.getAverageFulfillmentTime())
            .averageResponseTime(dto.getAverageResponseTime())
            .firstContactResolution(dto.getFirstContactResolution())
            .build();
    }

    default CountryMetricsDto.EconomicIndicatorsDto toEconomicIndicatorsDto(
            CountryMetrics.EconomicIndicators indicators) {
        if (indicators == null) {
            return null;
        }
        return CountryMetricsDto.EconomicIndicatorsDto.builder()
            .gdpGrowth(indicators.getGdpGrowth())
            .inflationRate(indicators.getInflationRate())
            .unemploymentRate(indicators.getUnemploymentRate())
            .exchangeRate(indicators.getExchangeRate())
            .interestRate(indicators.getInterestRate())
            .consumerConfidenceIndex(indicators.getConsumerConfidenceIndex())
            .purchasingPowerIndex(indicators.getPurchasingPowerIndex())
            .build();
    }

    default CountryMetrics.EconomicIndicators toEconomicIndicators(
            CountryMetricsDto.EconomicIndicatorsDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.EconomicIndicators.builder()
            .gdpGrowth(dto.getGdpGrowth())
            .inflationRate(dto.getInflationRate())
            .unemploymentRate(dto.getUnemploymentRate())
            .exchangeRate(dto.getExchangeRate())
            .interestRate(dto.getInterestRate())
            .consumerConfidenceIndex(dto.getConsumerConfidenceIndex())
            .purchasingPowerIndex(dto.getPurchasingPowerIndex())
            .build();
    }

    default CountryMetricsDto.DataQualityScoreDto toDataQualityScoreDto(
            CountryMetrics.DataQualityScore score) {
        if (score == null) {
            return null;
        }
        return CountryMetricsDto.DataQualityScoreDto.builder()
            .completeness(score.getCompleteness())
            .accuracy(score.getAccuracy())
            .timeliness(score.getTimeliness())
            .consistency(score.getConsistency())
            .overallScore(score.getOverallScore())
            .build();
    }

    default CountryMetrics.DataQualityScore toDataQualityScore(
            CountryMetricsDto.DataQualityScoreDto dto) {
        if (dto == null) {
            return null;
        }
        return CountryMetrics.DataQualityScore.builder()
            .completeness(dto.getCompleteness())
            .accuracy(dto.getAccuracy())
            .timeliness(dto.getTimeliness())
            .consistency(dto.getConsistency())
            .overallScore(dto.getOverallScore())
            .build();
    }

    default String mapMetricsStatus(CountryMetrics.MetricsStatus status) {
        return status != null ? status.name() : null;
    }

    default CountryMetrics.MetricsStatus mapMetricsStatus(String status) {
        return status != null ? CountryMetrics.MetricsStatus.valueOf(status) : null;
    }
}
