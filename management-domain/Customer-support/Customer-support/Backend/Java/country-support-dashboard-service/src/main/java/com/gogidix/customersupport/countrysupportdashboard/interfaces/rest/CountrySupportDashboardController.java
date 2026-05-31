package com.gogidix.customersupport.countrysupportdashboard.interfaces.rest;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.application.service.CountrySupportDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/country-support-dashboard")
@RequiredArgsConstructor
@Tag(name = "Country Support Dashboard", description = "APIs for country-specific support metrics and regional statistics")
public class CountrySupportDashboardController {

    private final CountrySupportDashboardService countrySupportDashboardService;

    @GetMapping("/country-metrics")
    @Operation(summary = "Get all country metrics", description = "Retrieve all country specific metrics")
    public ResponseEntity<List<CountrySpecificMetricsResponseDto>> getAllCountryMetrics() {
        return ResponseEntity.ok(countrySupportDashboardService.getAllCountryMetrics());
    }

    @GetMapping("/country-metrics/{id}")
    @Operation(summary = "Get country metrics by ID", description = "Retrieve specific country metrics by ID")
    public ResponseEntity<CountrySpecificMetricsResponseDto> getCountryMetricsById(
            @Parameter(description = "Metrics ID") @PathVariable String id) {
        return ResponseEntity.ok(countrySupportDashboardService.getCountryMetricsById(id));
    }

    @GetMapping("/country-metrics/country/{countryCode}")
    @Operation(summary = "Get metrics by country code", description = "Retrieve metrics filtered by country code")
    public ResponseEntity<List<CountrySpecificMetricsResponseDto>> getMetricsByCountryCode(
            @Parameter(description = "Country code (e.g., US, UK, DE)") @PathVariable String countryCode) {
        return ResponseEntity.ok(countrySupportDashboardService.getMetricsByCountryCode(countryCode));
    }

    @GetMapping("/country-metrics/country/{countryCode}/latest")
    @Operation(summary = "Get latest metrics by country", description = "Retrieve the most recent metrics for a country")
    public ResponseEntity<CountrySpecificMetricsResponseDto> getLatestMetricsByCountryCode(
            @Parameter(description = "Country code") @PathVariable String countryCode) {
        return ResponseEntity.ok(countrySupportDashboardService.getLatestMetricsByCountryCode(countryCode));
    }

    @GetMapping("/country-metrics/country/{countryCode}/daterange")
    @Operation(summary = "Get metrics by country and date range", description = "Retrieve metrics for a country within a date range")
    public ResponseEntity<List<CountrySpecificMetricsResponseDto>> getMetricsByCountryCodeAndDateRange(
            @Parameter(description = "Country code") @PathVariable String countryCode,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(countrySupportDashboardService.getMetricsByCountryCodeAndDateRange(countryCode, startDate, endDate));
    }

    @GetMapping("/country-metrics/region/{region}")
    @Operation(summary = "Get metrics by region", description = "Retrieve metrics filtered by region")
    public ResponseEntity<List<CountrySpecificMetricsResponseDto>> getMetricsByRegion(
            @Parameter(description = "Region name") @PathVariable String region) {
        return ResponseEntity.ok(countrySupportDashboardService.getMetricsByRegion(region));
    }

    @PostMapping("/country-metrics")
    @Operation(summary = "Create country metrics", description = "Create new country specific metrics")
    public ResponseEntity<CountrySpecificMetricsResponseDto> createMetrics(
            @Valid @RequestBody CountrySpecificMetricsRequestDto request) {
        CountrySpecificMetricsResponseDto created = countrySupportDashboardService.createMetrics(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/country-metrics/{id}")
    @Operation(summary = "Update country metrics", description = "Update existing country specific metrics")
    public ResponseEntity<CountrySpecificMetricsResponseDto> updateMetrics(
            @Parameter(description = "Metrics ID") @PathVariable String id,
            @Valid @RequestBody CountrySpecificMetricsRequestDto request) {
        return ResponseEntity.ok(countrySupportDashboardService.updateMetrics(id, request));
    }

    @DeleteMapping("/country-metrics/{id}")
    @Operation(summary = "Delete country metrics", description = "Delete country specific metrics")
    public ResponseEntity<Void> deleteMetrics(
            @Parameter(description = "Metrics ID") @PathVariable String id) {
        countrySupportDashboardService.deleteMetrics(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/country-metadata/countries")
    @Operation(summary = "Get distinct country codes", description = "Retrieve list of all country codes")
    public ResponseEntity<List<String>> getDistinctCountryCodes() {
        return ResponseEntity.ok(countrySupportDashboardService.getDistinctCountryCodes());
    }

    @GetMapping("/country-metadata/regions")
    @Operation(summary = "Get distinct regions", description = "Retrieve list of all regions")
    public ResponseEntity<List<String>> getDistinctRegions() {
        return ResponseEntity.ok(countrySupportDashboardService.getDistinctRegions());
    }

    @GetMapping("/regional-stats")
    @Operation(summary = "Get all regional stats", description = "Retrieve all regional ticket statistics")
    public ResponseEntity<List<RegionalTicketStatsResponseDto>> getAllRegionalStats() {
        return ResponseEntity.ok(countrySupportDashboardService.getAllRegionalStats());
    }

    @GetMapping("/regional-stats/{id}")
    @Operation(summary = "Get regional stats by ID", description = "Retrieve specific regional stats by ID")
    public ResponseEntity<RegionalTicketStatsResponseDto> getRegionalStatsById(
            @Parameter(description = "Stats ID") @PathVariable String id) {
        return ResponseEntity.ok(countrySupportDashboardService.getRegionalStatsById(id));
    }

    @GetMapping("/regional-stats/region/{regionName}")
    @Operation(summary = "Get stats by region", description = "Retrieve statistics filtered by region name")
    public ResponseEntity<List<RegionalTicketStatsResponseDto>> getStatsByRegion(
            @Parameter(description = "Region name") @PathVariable String regionName) {
        return ResponseEntity.ok(countrySupportDashboardService.getStatsByRegion(regionName));
    }

    @GetMapping("/regional-stats/region/{regionName}/latest")
    @Operation(summary = "Get latest stats by region", description = "Retrieve the most recent statistics for a region")
    public ResponseEntity<RegionalTicketStatsResponseDto> getLatestStatsByRegion(
            @Parameter(description = "Region name") @PathVariable String regionName) {
        return ResponseEntity.ok(countrySupportDashboardService.getLatestStatsByRegion(regionName));
    }

    @GetMapping("/regional-stats/daterange")
    @Operation(summary = "Get stats by date range", description = "Retrieve regional statistics within a date range")
    public ResponseEntity<List<RegionalTicketStatsResponseDto>> getStatsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(countrySupportDashboardService.getStatsByDateRange(startDate, endDate));
    }

    @GetMapping("/regional-metadata/regions")
    @Operation(summary = "Get distinct region names", description = "Retrieve list of all region names")
    public ResponseEntity<List<String>> getDistinctRegionNames() {
        return ResponseEntity.ok(countrySupportDashboardService.getDistinctRegionNames());
    }
}
