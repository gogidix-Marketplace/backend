package com.gogidix.customersupport.feedback.interfaces.rest;

import com.gogidix.customersupport.feedback.application.dto.response.NPSMetricResponseDto;
import com.gogidix.customersupport.feedback.application.service.NPSMetricService;
import com.gogidix.customersupport.feedback.domain.model.NPSMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for NPS Metric management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/metrics/nps")
@RequiredArgsConstructor
@Tag(name = "NPS Metric Management", description = "APIs for managing NPS metrics")
public class NPSMetricController {

    private final NPSMetricService metricService;

    @GetMapping
    @Operation(summary = "Get all NPS metrics", description = "Retrieve all NPS metrics")
    public ResponseEntity<List<NPSMetricResponseDto>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get NPS metric by ID", description = "Retrieve a specific NPS metric by ID")
    public ResponseEntity<NPSMetricResponseDto> getMetricById(
            @Parameter(description = "Metric ID") @PathVariable String id) {
        return ResponseEntity.ok(metricService.getMetricById(id));
    }

    @GetMapping("/period-type/{periodType}")
    @Operation(summary = "Get NPS metrics by period type", description = "Retrieve NPS metrics filtered by period type")
    public ResponseEntity<List<NPSMetricResponseDto>> getMetricsByPeriodType(
            @Parameter(description = "Period type") @PathVariable NPSMetric.PeriodType periodType) {
        return ResponseEntity.ok(metricService.getMetricsByPeriodType(periodType));
    }

    @GetMapping("/latest/{periodType}")
    @Operation(summary = "Get latest NPS metric", description = "Retrieve the latest NPS metric for a period type")
    public ResponseEntity<NPSMetricResponseDto> getLatestMetric(
            @Parameter(description = "Period type") @PathVariable NPSMetric.PeriodType periodType) {
        NPSMetricResponseDto metric = metricService.getLatestMetric(periodType);
        return ResponseEntity.ok(metric);
    }

    @GetMapping("/country/{countryCode}")
    @Operation(summary = "Get NPS metrics by country", description = "Retrieve NPS metrics filtered by country")
    public ResponseEntity<List<NPSMetricResponseDto>> getMetricsByCountry(
            @Parameter(description = "Country code") @PathVariable String countryCode) {
        return ResponseEntity.ok(metricService.getMetricsByCountry(countryCode));
    }

    @GetMapping("/agent/{agentId}")
    @Operation(summary = "Get NPS metrics by agent", description = "Retrieve NPS metrics filtered by agent")
    public ResponseEntity<List<NPSMetricResponseDto>> getMetricsByAgent(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(metricService.getMetricsByAgent(agentId));
    }

    @PostMapping
    @Operation(summary = "Create NPS metric", description = "Create a new NPS metric")
    public ResponseEntity<NPSMetricResponseDto> createMetric(
            @Parameter(description = "Period type") @RequestParam NPSMetric.PeriodType periodType,
            @Parameter(description = "Period start") @RequestParam long periodStartMillis,
            @Parameter(description = "Period end") @RequestParam long periodEndMillis) {
        NPSMetricResponseDto created = metricService.createMetric(
                periodType,
                java.time.Instant.ofEpochMilli(periodStartMillis),
                java.time.Instant.ofEpochMilli(periodEndMillis)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{metricId}/calculate")
    @Operation(summary = "Calculate NPS metric", description = "Calculate the NPS metric")
    public ResponseEntity<NPSMetricResponseDto> calculateMetric(
            @Parameter(description = "Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(metricService.calculateMetric(metricId));
    }

    @PostMapping("/generate/daily")
    @Operation(summary = "Generate daily NPS metric", description = "Generate a daily NPS metric")
    public ResponseEntity<NPSMetricResponseDto> generateDailyMetric(
            @Parameter(description = "Country code") @RequestParam(required = false) String countryCode,
            @Parameter(description = "Agent ID") @RequestParam(required = false) String agentId) {
        NPSMetricResponseDto created = metricService.generateDailyMetric(countryCode, agentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/generate/weekly")
    @Operation(summary = "Generate weekly NPS metric", description = "Generate a weekly NPS metric")
    public ResponseEntity<NPSMetricResponseDto> generateWeeklyMetric(
            @Parameter(description = "Country code") @RequestParam(required = false) String countryCode,
            @Parameter(description = "Team ID") @RequestParam(required = false) String teamId) {
        NPSMetricResponseDto created = metricService.generateWeeklyMetric(countryCode, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/generate/monthly")
    @Operation(summary = "Generate monthly NPS metric", description = "Generate a monthly NPS metric")
    public ResponseEntity<NPSMetricResponseDto> generateMonthlyMetric(
            @Parameter(description = "Country code") @RequestParam(required = false) String countryCode) {
        NPSMetricResponseDto created = metricService.generateMonthlyMetric(countryCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete NPS metric", description = "Delete an NPS metric")
    public ResponseEntity<Void> deleteMetric(
            @Parameter(description = "Metric ID") @PathVariable String id) {
        metricService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}
