package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DiversityResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.DiversityQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.DiversityMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for Diversity Metrics
 * Provides 10 endpoints for managing diversity data
 */
@RestController
@RequestMapping("/diversity-metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Diversity Metrics", description = "API for managing HR diversity metrics")
public class DiversityController {

    private final DiversityQueryService diversityQueryService;

    @GetMapping
    @Operation(summary = "Get all diversity metrics", description = "Returns all diversity metrics for the current tenant")
    public ResponseEntity<List<DiversityResponseDto>> getAllDiversityMetrics() {
        List<DiversityMetric> metrics = diversityQueryService.getAllDiversityMetrics();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get diversity by country", description = "Returns diversity metrics for a specific country")
    public ResponseEntity<List<DiversityResponseDto>> getDiversityByCountry(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode) {
        List<DiversityMetric> metrics = diversityQueryService.getDiversityByCountry(countryCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/country/{countryCode}/period/{period}")
    @Operation(summary = "Get diversity by country and period", description = "Returns diversity metrics for a country in a specific period")
    public ResponseEntity<DiversityResponseDto> getDiversityByCountryAndPeriod(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Period (e.g., 2024-01)", required = true)
            @PathVariable String period) {
        DiversityMetric metric = diversityQueryService.getDiversityByCountryAndPeriod(countryCode, period);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/by-region/{regionCode}")
    @Operation(summary = "Get diversity by region", description = "Returns diversity metrics for a specific region")
    public ResponseEntity<List<DiversityResponseDto>> getDiversityByRegion(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode) {
        List<DiversityMetric> metrics = diversityQueryService.getDiversityByRegion(regionCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get diversity by period", description = "Returns all diversity metrics for a specific period")
    public ResponseEntity<List<DiversityResponseDto>> getDiversityByPeriod(
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<DiversityMetric> metrics = diversityQueryService.getDiversityByPeriod(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get diversity summary", description = "Returns summary statistics for diversity metrics")
    public ResponseEntity<DiversityQueryService.DiversitySummary> getDiversitySummary(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        DiversityQueryService.DiversitySummary summary = diversityQueryService.getDiversitySummary(period);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/average-scores")
    @Operation(summary = "Get average diversity scores", description = "Returns average gender, national and overall diversity scores")
    public ResponseEntity<AverageScoresDto> getAverageScores(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        Double genderScore = diversityQueryService.getAverageGenderDiversityScore(period);
        Double nationalScore = diversityQueryService.getAverageNationalDiversityScore(period);
        Double overallScore = diversityQueryService.getAverageOverallDiversityScore(period);

        AverageScoresDto scores = new AverageScoresDto(genderScore, nationalScore, overallScore);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/high-gender-diversity")
    @Operation(summary = "Get countries with high gender diversity", description = "Returns countries with gender diversity above threshold")
    public ResponseEntity<List<DiversityResponseDto>> getWithHighGenderDiversity(
            @Parameter(description = "Minimum score")
            @RequestParam(defaultValue = "50.0") Double minScore) {
        List<DiversityMetric> metrics = diversityQueryService.getWithHighGenderDiversity(minScore);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/high-national-diversity")
    @Operation(summary = "Get countries with high national diversity", description = "Returns countries with national diversity above threshold")
    public ResponseEntity<List<DiversityResponseDto>> getWithHighNationalDiversity(
            @Parameter(description = "Minimum score")
            @RequestParam(defaultValue = "60.0") Double minScore) {
        List<DiversityMetric> metrics = diversityQueryService.getWithHighNationalDiversity(minScore);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/top-performing")
    @Operation(summary = "Get top performing countries", description = "Returns countries with highest overall diversity scores")
    public ResponseEntity<List<DiversityResponseDto>> getTopPerformingCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<DiversityMetric> metrics = diversityQueryService.getTopCountriesByDiversity(period, limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/good-gender-balance")
    @Operation(summary = "Get countries with good gender balance", description = "Returns countries with good gender balance")
    public ResponseEntity<List<DiversityResponseDto>> getCountriesWithGoodGenderBalance(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<DiversityMetric> metrics = diversityQueryService.getCountriesWithGoodGenderBalance(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/global-aggregate")
    @Operation(summary = "Get global diversity aggregate", description = "Returns aggregated global diversity metrics")
    public ResponseEntity<DiversityResponseDto> getGlobalAggregate(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        DiversityMetric metric = diversityQueryService.getGlobalAggregate(period);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/{countryCode}/trend")
    @Operation(summary = "Get diversity trend for country", description = "Returns diversity trend over time for a country")
    public ResponseEntity<List<DiversityResponseDto>> getDiversityTrend(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<DiversityMetric> metrics = diversityQueryService.getDiversityTrendByCountry(
                countryCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/region/{regionCode}/trend")
    @Operation(summary = "Get diversity trend for region", description = "Returns diversity trend over time for a region")
    public ResponseEntity<List<DiversityResponseDto>> getRegionTrend(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<DiversityMetric> metrics = diversityQueryService.getDiversityTrendByRegion(
                regionCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/gender-distribution")
    @Operation(summary = "Get global gender distribution", description = "Returns aggregated gender distribution across all countries")
    public ResponseEntity<Map<String, Integer>> getGlobalGenderDistribution(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<DiversityMetric> metrics = diversityQueryService.getDiversityByPeriod(period);

        Map<String, Integer> globalDistribution = metrics.stream()
                .filter(m -> m.getGenderDistribution() != null)
                .flatMap(m -> m.getGenderDistribution().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        return ResponseEntity.ok(globalDistribution);
    }

    private List<DiversityResponseDto> toResponseDtoList(List<DiversityMetric> metrics) {
        return metrics.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private DiversityResponseDto toResponseDto(DiversityMetric metric) {
        return DiversityResponseDto.builder()
                .id(metric.getId())
                .countryCode(metric.getCountryCode())
                .countryName(metric.getCountryName())
                .regionCode(metric.getRegionCode())
                .period(metric.getPeriod())
                .totalEmployees(metric.getTotalEmployees())
                .genderDistribution(metric.getGenderDistribution())
                .ageDistribution(metric.getAgeDistribution())
                .nationalityDistribution(metric.getNationalityDistribution())
                .ethnicityDistribution(metric.getEthnicityDistribution())
                .educationLevelDistribution(metric.getEducationLevelDistribution())
                .genderDiversityScore(metric.getGenderDiversityScore())
                .nationalDiversityScore(metric.getNationalDiversityScore())
                .overallDiversityScore(metric.getOverallDiversityScore())
                .womenInLeadershipPercentage(metric.getWomenInLeadershipPercentage())
                .womenInLeadershipCount(metric.getWomenInLeadershipCount())
                .totalLeadershipCount(metric.getTotalLeadershipCount())
                .isActive(metric.getIsActive())
                .lastUpdated(metric.getLastUpdated())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public record AverageScoresDto(
            Double genderDiversityScore,
            Double nationalDiversityScore,
            Double overallDiversityScore
    ) {}
}
