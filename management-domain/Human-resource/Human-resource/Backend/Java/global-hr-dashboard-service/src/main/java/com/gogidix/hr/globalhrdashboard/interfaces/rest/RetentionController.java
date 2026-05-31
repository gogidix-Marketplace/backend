package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.RetentionResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.RetentionQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.RetentionMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Retention Metrics
 * Provides 10 endpoints for managing retention data
 */
@RestController
@RequestMapping("/retention-metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Retention Metrics", description = "API for managing HR retention metrics")
public class RetentionController {

    private final RetentionQueryService retentionQueryService;

    @GetMapping
    @Operation(summary = "Get all retention metrics", description = "Returns all retention metrics for the current tenant")
    public ResponseEntity<List<RetentionResponseDto>> getAllRetentionMetrics() {
        List<RetentionMetric> metrics = retentionQueryService.getAllRetentionMetrics();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get retention by country", description = "Returns retention metrics for a specific country")
    public ResponseEntity<List<RetentionResponseDto>> getRetentionByCountry(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode) {
        List<RetentionMetric> metrics = retentionQueryService.getRetentionByCountry(countryCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/country/{countryCode}/period/{period}")
    @Operation(summary = "Get retention by country and period", description = "Returns retention metrics for a country in a specific period")
    public ResponseEntity<RetentionResponseDto> getRetentionByCountryAndPeriod(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Period (e.g., 2024-01)", required = true)
            @PathVariable String period) {
        RetentionMetric metric = retentionQueryService.getRetentionByCountryAndPeriod(countryCode, period);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/by-region/{regionCode}")
    @Operation(summary = "Get retention by region", description = "Returns retention metrics for a specific region")
    public ResponseEntity<List<RetentionResponseDto>> getRetentionByRegion(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode) {
        List<RetentionMetric> metrics = retentionQueryService.getRetentionByRegion(regionCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get retention by period", description = "Returns all retention metrics for a specific period")
    public ResponseEntity<List<RetentionResponseDto>> getRetentionByPeriod(
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<RetentionMetric> metrics = retentionQueryService.getRetentionByPeriod(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get retention summary", description = "Returns summary statistics for retention metrics")
    public ResponseEntity<RetentionQueryService.RetentionSummary> getRetentionSummary(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        RetentionQueryService.RetentionSummary summary = retentionQueryService.getRetentionSummary(period);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/average-rates")
    @Operation(summary = "Get average retention and turnover rates", description = "Returns average retention and turnover rates")
    public ResponseEntity<AverageRatesDto> getAverageRates(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        Double retentionRate = retentionQueryService.getAverageRetentionRate(period);
        Double turnoverRate = retentionQueryService.getAverageTurnoverRate(period);
        Double avgTenure = retentionQueryService.getGlobalAverageTenure(period);

        AverageRatesDto rates = new AverageRatesDto(retentionRate, turnoverRate, avgTenure);
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/healthy-retention")
    @Operation(summary = "Get countries with healthy retention", description = "Returns countries with healthy retention rates")
    public ResponseEntity<List<RetentionResponseDto>> getCountriesWithHealthyRetention(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<RetentionMetric> metrics = retentionQueryService.getCountriesWithHealthyRetention(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/high-turnover")
    @Operation(summary = "Get countries with high turnover", description = "Returns countries with high turnover rates")
    public ResponseEntity<List<RetentionResponseDto>> getCountriesWithHighTurnover(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<RetentionMetric> metrics = retentionQueryService.getCountriesWithHighTurnover(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/top-performing")
    @Operation(summary = "Get top performing countries", description = "Returns countries with highest retention rates")
    public ResponseEntity<List<RetentionResponseDto>> getTopPerformingCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<RetentionMetric> metrics = retentionQueryService.getTopCountriesByRetention(period, limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/bottom-performing")
    @Operation(summary = "Get bottom performing countries", description = "Returns countries with lowest retention rates")
    public ResponseEntity<List<RetentionResponseDto>> getBottomPerformingCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<RetentionMetric> metrics = retentionQueryService.getBottomCountriesByRetention(period, limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/global-aggregate")
    @Operation(summary = "Get global retention aggregate", description = "Returns aggregated global retention metrics")
    public ResponseEntity<RetentionResponseDto> getGlobalAggregate(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        RetentionMetric metric = retentionQueryService.getGlobalAggregate(period);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/{countryCode}/trend")
    @Operation(summary = "Get retention trend for country", description = "Returns retention trend over time for a country")
    public ResponseEntity<List<RetentionResponseDto>> getRetentionTrend(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<RetentionMetric> metrics = retentionQueryService.getRetentionTrendByCountry(
                countryCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/region/{regionCode}/trend")
    @Operation(summary = "Get retention trend for region", description = "Returns retention trend over time for a region")
    public ResponseEntity<List<RetentionResponseDto>> getRegionTrend(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<RetentionMetric> metrics = retentionQueryService.getRetentionTrendByRegion(
                regionCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/global-rates")
    @Operation(summary = "Get global retention and turnover rates", description = "Returns calculated global retention and turnover rates")
    public ResponseEntity<GlobalRatesDto> getGlobalRates(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        Double retentionRate = retentionQueryService.getGlobalRetentionRate(period);
        Double turnoverRate = retentionQueryService.getGlobalTurnoverRate(period);

        GlobalRatesDto rates = new GlobalRatesDto(retentionRate, turnoverRate);
        return ResponseEntity.ok(rates);
    }

    private List<RetentionResponseDto> toResponseDtoList(List<RetentionMetric> metrics) {
        return metrics.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private RetentionResponseDto toResponseDto(RetentionMetric metric) {
        return RetentionResponseDto.builder()
                .id(metric.getId())
                .countryCode(metric.getCountryCode())
                .countryName(metric.getCountryName())
                .regionCode(metric.getRegionCode())
                .period(metric.getPeriod())
                .retentionRate(metric.getRetentionRate())
                .turnoverRate(metric.getTurnoverRate())
                .totalEmployees(metric.getTotalEmployees())
                .voluntaryDepartures(metric.getVoluntaryDepartures())
                .involuntaryDepartures(metric.getInvoluntaryDepartures())
                .avgTenure(metric.getAvgTenure())
                .medianTenure(metric.getMedianTenure())
                .departureReasons(metric.getDepartureReasons() != null ?
                        metric.getDepartureReasons().stream()
                                .map(dr -> RetentionResponseDto.DepartureReasonDto.builder()
                                        .reason(dr.getReason())
                                        .category(dr.getCategory())
                                        .count(dr.getCount())
                                        .percentage(dr.getPercentage())
                                        .preventable(dr.isPreventable())
                                        .trend(dr.getTrend())
                                        .build())
                                .collect(Collectors.toList()) : null)
                .newHireRetentionRate(metric.getNewHireRetentionRate())
                .totalNewHires(metric.getTotalNewHires())
                .retainedNewHires(metric.getRetainedNewHires())
                .topPerformerRetentionRate(metric.getTopPerformerRetentionRate())
                .totalTopPerformers(metric.getTotalTopPerformers())
                .retainedTopPerformers(metric.getRetainedTopPerformers())
                .promotionRate(metric.getPromotionRate())
                .totalPromotions(metric.getTotalPromotions())
                .internalMobilityRate(metric.getInternalMobilityRate())
                .isActive(metric.getIsActive())
                .lastUpdated(metric.getLastUpdated())
                .notes(metric.getNotes())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public record AverageRatesDto(
            Double averageRetentionRate,
            Double averageTurnoverRate,
            Double averageTenure
    ) {}

    public record GlobalRatesDto(
            Double globalRetentionRate,
            Double globalTurnoverRate
    ) {}
}
