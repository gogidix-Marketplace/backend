package com.gogidix.platform.metering.interfaces.rest;

import com.gogidix.platform.metering.application.dto.MetricDefinitionDto;
import com.gogidix.platform.metering.application.service.MetricDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for metric definition operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "Metric Definitions", description = "Metric definition and catalog APIs")
public class MetricDefinitionController {

    private final MetricDefinitionService metricDefinitionService;

    @PostMapping
    @Operation(summary = "Create metric definition", description = "Create a new metric definition")
    public ResponseEntity<MetricDefinitionDto> createMetric(@Valid @RequestBody MetricDefinitionDto dto) {
        log.info("Creating metric definition: name={}", dto.getMetricName());
        MetricDefinitionDto result = metricDefinitionService.createMetricDefinition(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(summary = "Get all metrics", description = "Get all metric definitions")
    public ResponseEntity<List<MetricDefinitionDto>> getAllMetrics() {
        return ResponseEntity.ok(metricDefinitionService.getAllMetrics());
    }

    @GetMapping("/active")
    @Operation(summary = "Get active metrics", description = "Get all active metric definitions")
    public ResponseEntity<List<MetricDefinitionDto>> getActiveMetrics() {
        return ResponseEntity.ok(metricDefinitionService.getActiveMetrics());
    }

    @GetMapping("/{metricName}")
    @Operation(summary = "Get metric by name", description = "Get a specific metric definition by name")
    public ResponseEntity<MetricDefinitionDto> getMetric(@PathVariable String metricName) {
        return ResponseEntity.ok(metricDefinitionService.getMetricDefinition(metricName));
    }
}
